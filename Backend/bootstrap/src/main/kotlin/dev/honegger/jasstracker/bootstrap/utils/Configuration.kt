package dev.honegger.jasstracker.bootstrap.utils

inline fun readConfiguration(getEnvVariable: (name: String) -> String?) = listOf(
    "jasstracker.db.url" to (getEnvVariable("DB_URL") ?: "jdbc:postgresql://localhost:5432/jasstracker"),
    "jasstracker.db.user" to (getEnvVariable("DB_USER") ?: "jasstracker"),
    "jasstracker.db.password" to (getEnvVariable("DB_PASSWORD") ?: "password"),

    "jwt.secret" to (getEnvVariable("JWT_SECRET") ?: "z8TxaimeeD3R9EFBCBTLNi6LNlDOOiRuKjb5TYcUEcNNjYDzhbS5StLIB1wqvDPhNoXY66FUvIsQrOykDUbUQg=="),
    "jwt.issuer" to (getEnvVariable("JWT_ISSUER") ?: "http://0.0.0.0:8080/"),
    "jwt.audience" to (getEnvVariable("JWT_AUDIENCE") ?: "http://0.0.0.0:9090/"),
    "jwt.realm" to (getEnvVariable("JWT_REALM") ?: "JassTracker"),
    "jwt.expiryTime" to (getEnvVariable("JWT_EXPIRY_TIME") ?: "4h"),

    "hash.iterations" to (getEnvVariable("HASH_ITERATIONS") ?: "10"),
    "hash.memory" to (getEnvVariable("HASH_MEMORY") ?: "65536"),
    "hash.parallelization" to (getEnvVariable("HASH_PARALLELIZATION") ?: "1"),
)
