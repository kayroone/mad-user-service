# Projektkontext: User-Service (MAD-PoC)

## Ziel

Proof of Concept für Maximal Agentic Decomposition (MAD) - ein minimaler CRUD-Service für User-Verwaltung.

## Tech-Stack

- Java 21, Spring Boot 3.2.x
- MySQL 8.0, Spring Data JPA
- Bean Validation (jakarta.validation)
- Lombok für Boilerplate-Reduktion
- Maven als Build-Tool

## Architektur

- Package-Struktur: ECB-Pattern (Entity, Control, Boundary)
  - `entity/` - JPA Entities und Repositories
  - `control/` - Service Interfaces und Implementierungen
  - `boundary/` - REST Controller und DTOs
- DTOs für API-Kommunikation, Entities für Persistenz
- Mapper-Klasse für Entity ↔ DTO Konvertierung

## Constraints

- Keine Records verwenden (Lombok @Data stattdessen)
- Constructor Injection mit @RequiredArgsConstructor
- Fehlerbehandlung zentral via @ControllerAdvice
- Keine Javadoc oder ausführliche Kommentare
- Max. 30 Zeilen pro Klasse (wo möglich)

## API-Design

- REST-Konventionen: /api/users
- HTTP-Methoden: GET, POST, PUT, DELETE
- Response-Codes: 200, 201, 400, 404, 500

## Nicht verwenden

- @Builder, @AllArgsConstructor
- Records (Java 14+)
- MapStruct oder andere Mapping-Frameworks
- Spring Security (außerhalb Scope)
