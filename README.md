# Ktor Authentication Service

A Kotlin/Ktor-based authentication service that provides JWT token generation and JWKS (JSON Web Key Set) endpoint for token verification.

## Features

- **JWT Authentication**: RSA256-signed JWT tokens for secure authentication
- **Login Endpoint**: Authenticate with username/password to receive access tokens
- **JWKS Endpoint**: Public key distribution for token verification by other services
- **Health Check**: Simple health monitoring endpoint
- **Configuration Management**: YAML-based configuration with environment variable support

## Tech Stack

- **Kotlin**: 2.4.0
- **Ktor**: 3.5.2
- **Server**: Netty
- **JWT Library**: Auth0 Java JWT 4.5.0
- **Serialization**: Kotlinx Serialization (JSON)
- **Logging**: Logback 1.5.37
- **Build**: Gradle with Kotlin DSL

## Project Structure

```
src/
└── main/
    ├── kotlin/com/example/auth/
    │   ├── Application.kt    # Main application module
    │   ├── auth/
    │   │   ├── JwtKey.kt    # RSA key pair data class
    │   │   └── JwtService.kt # JWT token generation service
    │   ├── config/
    │   │   └── AppConfig.kt  # Application configuration
    │   ├── model/
    │   │   ├── JwksResponse.kt   # JWKS response models
    │   │   ├── LoginRequest.kt   # Login request model
    │   │   └── TokenResponse.kt  # Token response model
    │   └── routing/
    │       ├── AuthRoutes.kt     # Authentication routes
    │       └── JwksRoutes.kt     # JWKS endpoint routes
    └── resources/
        ├── application.yaml      # Application configuration
        └── logback.xml           # Logging configuration
```

## Configuration

Edit `src/main/resources/application.yaml`:

```yaml
ktor:
  application:
    modules:
      - com.example.auth.ApplicationKt.module
  deployment:
    port: "$PORT:8080"

auth:
  issuer: "ktor-auth-service"
  audience: "my-microservices"
  realm: "ktor-auth"
  username: "$AUTH_USER"
  password: "$AUTH_PASSWORD"
  accessTokenTtlMs: 3600000  # 1 hour
```

Environment variables can override defaults using the `$VAR:default` syntax.

## Running the Application

### Using Gradle Wrapper

```bash
./gradlew run
```

### Using Gradle (if installed globally)

```bash
gradle run
```

The server will start on port 8080 (or the PORT environment variable if set).

## API Endpoints

### POST /login

Authenticate with username and password.

**Example:**
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJraWQiOiJraWQtMjAyNi12MSIsImFsZyI6IlJTMjU2IiwidHlwIjoiSldUIn0.eyJpc3MiOiJrdG9yLWF1dGgtc2VydmljZSIsImF1ZCI6Im15LW1pY3Jvc2VydmljZXMiLCJzdWIiOiJhZG1pbiIsImlhdCI6MTc4OTgzNjI0NCwiZXhwIjoxNzg5ODM5ODQ0fQ...",
  "tokenType": "Bearer"
}
```

**Error Responses:**
- `400 Bad Request`: Invalid request body
- `401 Unauthorized`: Invalid credentials

```bash
# Example of invalid credentials
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrong-password"}'
# Response: {"error":"Invalid credentials"}
```

### GET /.well-known/jwks.json

Retrieve the public key for JWT verification (JWKS format).

**Example:**
```bash
curl http://localhost:8080/.well-known/jwks.json
```

**Response (200 OK):**
```json
{
  "keys": [
    {
      "kty": "RSA",
      "kid": "kid-2026-v1",
      "alg": "RS256",
      "use": "sig",
      "n": "tNW6NRvvgFebmMLNKzIEkbYqyUU1LFmhe-bJBs5_67Vb4i-OGDoYW-OzhEeo3gPNJrRR0S-f_97ti...",
      "e": "AQAB"
    }
  ]
}
```

### GET /health

Health check endpoint.

**Response (200 OK):**
```json
{
  "status": "UP"
}
```

## JWT Token Details

- **Algorithm**: RS256
- **Key Size**: 2048 bits
- **Token TTL**: 1 hour (configurable via `accessTokenTtlMs`)
- **Claims**: issuer, audience, subject (username), issued at, expires at, key ID

