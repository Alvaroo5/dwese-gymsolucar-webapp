package org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.controllers;

import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos.RankingDTO;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos.ProgressStatsDTO;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.dtos.ProgressHistoryDTO;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Clase;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.ReservaClase;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.entities.Usuario;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.ClaseRepository;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.ReservaClaseRepository;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.repositories.UsuarioRepository;
import org.iesalixar.daw2.alvarolabradorgarcia.dwese_gymsolucar_webapp.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('USER', 'GESTOR', 'ADMIN')")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UsuarioRepository usuarioRepository;
    private final ClaseRepository claseRepository;
    private final ReservaClaseRepository reservaClaseRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public UserController(UsuarioRepository usuarioRepository, ClaseRepository claseRepository, ReservaClaseRepository reservaClaseRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.claseRepository = claseRepository;
        this.reservaClaseRepository = reservaClaseRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> getPerfil() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("id", usuario.getId());
        perfil.put("username", usuario.getUsername());
        perfil.put("nombre", usuario.getNombre());
        perfil.put("apellidos", usuario.getApellidos());
        perfil.put("email", usuario.getEmail());
        perfil.put("fechaNacimiento", usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toString() : null);
        perfil.put("telefono", usuario.getTelefono());
        perfil.put("hasPassword", usuario.getPassword() != null && !usuario.getPassword().isEmpty());

        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean exists = usuarioRepository.existsByUsername(username) && !username.equals(currentUsername);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        String currentEmail = usuarioRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(Usuario::getEmail)
                .orElse(null);
        boolean exists = usuarioRepository.findByEmail(email).isPresent() && !email.equals(currentEmail);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PutMapping("/perfil")
    public ResponseEntity<Map<String, Object>> updatePerfil(@RequestBody Map<String, Object> updatedProfile) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String newUsername = (String) updatedProfile.get("username");
        if (newUsername != null) {
            if (usuarioRepository.existsByUsername(newUsername) && !newUsername.equals(currentUsername)) {
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre de usuario ya está registrado."));
            }
            usuario.setUsername(newUsername);
        }
        if (updatedProfile.containsKey("email")) {
            String newEmail = (String) updatedProfile.get("email");
            if (usuarioRepository.findByEmail(newEmail).isPresent() && !newEmail.equals(usuario.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("error", "El email ya está registrado."));
            }
            usuario.setEmail(newEmail);
        }
        if (updatedProfile.containsKey("nombre")) {
            usuario.setNombre((String) updatedProfile.get("nombre"));
        }
        if (updatedProfile.containsKey("apellidos")) {
            usuario.setApellidos((String) updatedProfile.get("apellidos"));
        }
        if (updatedProfile.containsKey("fechaNacimiento")) {
            String fechaNacimiento = (String) updatedProfile.get("fechaNacimiento");
            usuario.setFechaNacimiento(fechaNacimiento != null ? java.time.LocalDate.parse(fechaNacimiento) : null);
        }
        if (updatedProfile.containsKey("telefono")) {
            usuario.setTelefono((String) updatedProfile.get("telefono"));
        }
        if (updatedProfile.containsKey("newPassword") && updatedProfile.containsKey("confirmPassword")) {
            String newPassword = (String) updatedProfile.get("newPassword");
            String confirmPassword = (String) updatedProfile.get("confirmPassword");
            if (newPassword != null && confirmPassword != null && newPassword.equals(confirmPassword)) {
                if (newPassword.length() < 6) {
                    return ResponseEntity.badRequest().body(Map.of("error", "La contraseña debe tener al menos 6 caracteres."));
                }
                usuario.setPassword(passwordEncoder.encode(newPassword));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Las contraseñas no coinciden."));
            }
        }

        usuarioRepository.save(usuario);

        String updatedUsername = newUsername != null ? newUsername : currentUsername;
        String newToken = jwtUtil.generateToken(updatedUsername, usuario.getRoles().stream().map(r -> r.getNombre()).toList());

        Map

                <String, Object> response = new HashMap<>();
        response.put("message", "Perfil actualizado correctamente.");
        response.put("newToken", newToken);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/clases")
    public ResponseEntity<List<Clase>> getClases() {
        List<Clase> clases = claseRepository.findAll();
        return ResponseEntity.ok(clases);
    }

    @GetMapping("/clases/{nombreClase}/horarios")
    public ResponseEntity<List<Map<String, Object>>> getClaseHorarios(@PathVariable String nombreClase) {
        List<Clase> horarios = claseRepository.findByNombreClaseIgnoreCase(nombreClase);
        if (horarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Map<String, Object>> response = horarios.stream().map(clase -> {
            Map<String, Object> claseMap = new HashMap<>();
            claseMap.put("id", clase.getId());
            claseMap.put("nombreClase", clase.getNombreClase());
            claseMap.put("diaSemana", clase.getDiaSemana());
            claseMap.put("horaInicio", clase.getHoraInicio());
            claseMap.put("horaFin", clase.getHoraFin());
            claseMap.put("aforoMaximo", clase.getAforoMaximo());
            long reservas = reservaClaseRepository.countByIdClase(clase.getId());
            claseMap.put("plazasOcupadas", reservas);
            claseMap.put("plazasDisponibles", clase.getAforoMaximo() - reservas);
            return claseMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservas")
    public ResponseEntity<Map<String, Object>> createReserva(@RequestBody Map<String, Object> reservaRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Integer claseId = (Integer) reservaRequest.get("claseId");
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        List<ReservaClase> reservasExistentes = reservaClaseRepository.findByIdUsuarioAndIdClase(usuario.getId(), claseId);
        if (!reservasExistentes.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya tienes reservada esta clase en ese horario."));
        }

        long reservasActuales = reservaClaseRepository.countByIdClase(claseId);
        if (clase.getAforoMaximo() <= reservasActuales) {
            return ResponseEntity.badRequest().body(Map.of("error", "No hay plazas disponibles para esta clase."));
        }

        ReservaClase reserva = new ReservaClase();
        reserva.setIdUsuario(usuario.getId());
        reserva.setIdClase(claseId);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setAsistenciaConfirmada(false);
        reservaClaseRepository.save(reserva);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reserva realizada con éxito.");
        response.put("claseId", clase.getId());
        response.put("claseNombre", clase.getNombreClase());
        response.put("diaSemana", clase.getDiaSemana());
        response.put("horaInicio", clase.getHoraInicio());
        response.put("horaFin", clase.getHoraFin());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<Map<String, Object>>> getReservas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<ReservaClase> reservas = reservaClaseRepository.findByIdUsuario(usuario.getId());
        List<Map<String, Object>> response = reservas.stream().map(reserva -> {
            Clase clase = claseRepository.findById(reserva.getIdClase())
                    .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
            Map<String, Object> reservaMap = new HashMap<>();
            reservaMap.put("id", reserva.getId());
            reservaMap.put("claseNombre", clase.getNombreClase());
            reservaMap.put("diaSemana", clase.getDiaSemana());
            reservaMap.put("horaInicio", clase.getHoraInicio());
            reservaMap.put("horaFin", clase.getHoraFin());
            return reservaMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservas/{reservaId}")
    public ResponseEntity<Map<String, Object>> cancelReserva(@PathVariable Integer reservaId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ReservaClase reserva = reservaClaseRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getIdUsuario().equals(usuario.getId())) {
            return ResponseEntity.status(403).body(Map.of("error", "No tienes permiso para cancelar esta reserva."));
        }

        Clase clase = claseRepository.findById(reserva.getIdClase())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        reservaClaseRepository.delete(reserva);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reserva cancelada con éxito.");
        response.put("claseNombre", clase.getNombreClase());
        response.put("diaSemana", clase.getDiaSemana());
        response.put("horaInicio", clase.getHoraInicio());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking")
    public ResponseEntity<Page<RankingDTO>> getRanking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String usernameFilter,
            @RequestParam(required = false) String topRange) {

        StringBuilder sql = new StringBuilder("SELECT vr.id_usuario, u.username, vr.puntos_totales " +
                "FROM vista_ranking vr " +
                "JOIN usuarios u ON vr.id_usuario = u.id " +
                "JOIN usuario_roles ur ON u.id = ur.id_usuario " +
                "WHERE ur.id_rol = 3");

        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            sql.append(" AND u.username LIKE ?");
        }

        String orderDirection = sortOrder.equalsIgnoreCase("asc") ? "ASC" : "DESC";
        sql.append(" ORDER BY vr.puntos_totales ").append(orderDirection).append(", vr.id_usuario ASC");

        String countSql = "SELECT COUNT(*) " +
                "FROM vista_ranking vr " +
                "JOIN usuarios u ON vr.id_usuario = u.id " +
                "JOIN usuario_roles ur ON u.id = ur.id_usuario " +
                "WHERE ur.id_rol = 3";
        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            countSql += " AND u.username LIKE ?";
        }

        Long total;
        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            total = jdbcTemplate.queryForObject(countSql, Long.class, "%" + usernameFilter + "%");
        } else {
            total = jdbcTemplate.queryForObject(countSql, Long.class);
        }

        String sqlWithPagination = sql + " LIMIT ? OFFSET ?";
        List<RankingDTO> rankingList;
        int offset = page * size;
        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            rankingList = jdbcTemplate.query(
                    sqlWithPagination,
                    (rs, rowNum) -> new RankingDTO(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            rs.getLong("puntos_totales")
                    ),
                    "%" + usernameFilter + "%", size, offset
            );
        } else {
            rankingList = jdbcTemplate.query(
                    sqlWithPagination,
                    (rs, rowNum) -> new RankingDTO(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            rs.getLong("puntos_totales")
                    ),
                    size, offset
            );
        }

        if (topRange != null && !topRange.isEmpty()) {
            String[] range = topRange.split("-");
            if (range.length == 2) {
                try {
                    int start = Integer.parseInt(range[0]) - 1;
                    int end = Integer.parseInt(range[1]);
                    if (start >= 0 && end > start && start < rankingList.size()) {
                        rankingList = rankingList.subList(start, Math.min(end, rankingList.size()));
                        total = (long) rankingList.size();
                    } else {
                        rankingList.clear();
                        total = 0L;
                    }
                } catch (NumberFormatException e) {
                    rankingList.clear();
                    total = 0L;
                }
            }
        }

        Page<RankingDTO> rankingPage = new PageImpl<>(rankingList, PageRequest.of(page, size), total);
        return ResponseEntity.ok(rankingPage);
    }

    @GetMapping("/progreso/stats")
    public ResponseEntity<ProgressStatsDTO> getProgressStats() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Long puntosAcumulados = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(puntos), 0) FROM progreso WHERE id_usuario = ?",
                Long.class,
                usuario.getId()
        );

        Long clasesAsistidas = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM progreso WHERE id_usuario = ? AND actividad = 'Acudir a una clase'",
                Long.class,
                usuario.getId()
        );

        Long ejerciciosRealizados = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM progreso WHERE id_usuario = ? AND actividad = 'Completar una rutina'",
                Long.class,
                usuario.getId()
        );

        ProgressStatsDTO stats = new ProgressStatsDTO(puntosAcumulados, clasesAsistidas, ejerciciosRealizados);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/progreso")
    public ResponseEntity<Page<ProgressHistoryDTO>> getProgressHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String sql = "SELECT fecha, actividad, puntos AS puntosObtenidos " +
                "FROM progreso " +
                "WHERE id_usuario = ? " +
                "ORDER BY fecha DESC " +
                "LIMIT ? OFFSET ?";
        int offset = page * size;

        List<ProgressHistoryDTO> history = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ProgressHistoryDTO(
                        rs.getString("fecha"),
                        rs.getString("actividad"),
                        rs.getLong("puntosObtenidos")
                ),
                usuario.getId(),
                size,
                offset
        );

        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM progreso WHERE id_usuario = ?",
                Long.class,
                usuario.getId()
        );

        Page<ProgressHistoryDTO> historyPage = new PageImpl<>(history, PageRequest.of(page, size), total);
        return ResponseEntity.ok(historyPage);
    }
}