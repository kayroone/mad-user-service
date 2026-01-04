# MAD User-Service - Proof of Concept

Dieser Service wurde als lightweight **Proof of Concept** für die **Maximal Agentic Decomposition (MAD)** Methode erstellt, wie im Blog-Artikel ["Warum gute Entwickler mit AI besser werden"](https://jwiegmann.de/blog/warum-gute-entwickler-mit-ai-besser-werden) beschrieben.

## Ziel

Demonstration der 5-Schritte-Methode für effektives Arbeiten mit KI-Tools:

1. **Context Engineering** - Projektkontext klar definieren
2. **MAD anwenden** - Aufgaben in minimale Einzelschritte zerlegen
3. **Output Requirements** - Präzise Spezifikationen pro Arbeitspaket
4. **Red-Flagging** - Warnsignale erkennen und reagieren
5. **Voting** - Kritische Entscheidungen absichern

---

## Phase 0: Projekt-Setup

### 0.1 Spring Boot Projekt initialisieren
- **Input:** Spring Initializr mit Java 21, Spring Boot 3.2, Dependencies: Web, JPA, MySQL, Lombok, Validation
- **Output:** Maven-Projekt mit `pom.xml`

### 0.2 Docker Compose für MySQL
- **Input:** MySQL 8.0 Konfiguration
- **Output:** `docker-compose.yml` mit MySQL Container

---

## Phase 1: Context Engineering

Siehe [`CONTEXT.md`](./CONTEXT.md) für den vollständigen Projektkontext.

**Kernpunkte:**
- **Tech-Stack:** Java 21, Spring Boot 3.2, MySQL 8.0, Lombok
- **Architektur:** ECB-Pattern (Entity, Control, Boundary)
- **Package:** `de.jwiegmann.userservice`

---

## Phase 2: MAD - Arbeitspakete

### Datenschicht

| # | Arbeitspaket | Output | Edge Cases |
|---|--------------|--------|------------|
| 2.1 | User Entity | `entity/User.java` | email unique |
| 2.2 | UserRepository | `entity/UserRepository.java` | findByEmail |

### Service Layer

| # | Arbeitspaket | Output | Edge Cases |
|---|--------------|--------|------------|
| 2.3 | UserService Interface | `control/UserService.java` | - |
| 2.4 | UserServiceImpl | `control/UserServiceImpl.java` | User nicht gefunden |

### API Layer

| # | Arbeitspaket | Output | Edge Cases |
|---|--------------|--------|------------|
| 2.5 | UserDTO | `boundary/UserDTO.java` | Validation |
| 2.6 | UserMapper | `boundary/UserMapper.java` | null-Handling |
| 2.7 | UserController | `boundary/UserController.java` | 404, 400 |
| 2.8 | GlobalExceptionHandler | `boundary/GlobalExceptionHandler.java` | Error-Response |

### Konfiguration & Tests

| # | Arbeitspaket | Output |
|---|--------------|--------|
| 2.9 | Application Properties | `application.yml` |
| 2.10 | UserServiceTest | `control/UserServiceTest.java` |
| 2.11 | UserControllerTest | `boundary/UserControllerTest.java` |

---

## Phase 3: Output Requirements

| Klasse | Max. Zeilen | Annotations | Nicht verwenden |
|--------|-------------|-------------|-----------------|
| User Entity | 25 | @Data, @Entity | @Builder |
| UserRepository | 10 | - | Custom Queries |
| UserService | 15 | - | Default Methods |
| UserServiceImpl | 40 | @Service, @RequiredArgsConstructor | @Autowired |
| UserDTO | 20 | @Data, Validation | Records |
| UserMapper | 20 | @Component | MapStruct |
| UserController | 50 | @RestController | @Controller |
| ExceptionHandler | 30 | @ControllerAdvice | - |

---

## Phase 4: Red-Flagging Kriterien

| Signal | Schwelle | Aktion |
|--------|----------|--------|
| Überlange Antwort | >50 Zeilen | STOPP, Prompt neu formulieren |
| Formatierungsfehler | Syntaxfehler | Neu generieren |
| Unerwünschte Patterns | @Builder, Records | Prompt präzisieren |
| Halluzinationen | Nicht existierende APIs | Verifizieren |
| Scope Creep | Extras | Zurückweisen |

---

## Phase 5: Voting-Entscheidungen

### 1. Exception-Handling Strategie
- **Frage:** Wie strukturieren wir Error-Responses?
- **Entscheidung:** **Einheitliches ErrorDTO** mit timestamp, status, message, path
- **Begründung:** Konsistente API-Responses erleichtern Client-Implementierung

### 2. DTO-Design
- **Frage:** Separate DTOs für Request/Response?
- **Entscheidung:** **Separate DTOs** (CreateUserRequest, UpdateUserRequest, UserResponse)
- **Begründung:** Explizit, welche Felder bei Create/Update erwartet werden vs. Response

### 3. Mapper-Implementierung
- **Frage:** Statische oder Instance-Methoden?
- **Entscheidung:** **Instance-Methoden** mit @Component
- **Begründung:** Testbar mit Mocking, Spring-konform

---

## Implementierungsreihenfolge

```
0.1 Projekt-Setup → 0.2 Docker Compose
          ↓
    1.0 CONTEXT.md
          ↓
  2.1 User Entity → 2.2 UserRepository
          ↓
2.3 UserService → 2.4 UserServiceImpl
          ↓
   2.5 UserDTO → 2.6 UserMapper
          ↓
2.7 UserController → 2.8 ExceptionHandler
          ↓
  2.9 Application Properties
          ↓
2.10 UserServiceTest → 2.11 UserControllerTest
          ↓
      Tests + Manueller Test
```

---

## Quickstart

```bash
# MySQL starten
docker-compose up -d

# Anwendung starten
./mvnw spring-boot:run

# API testen
curl http://localhost:8080/api/users
```

---

## Lizenz

MIT
