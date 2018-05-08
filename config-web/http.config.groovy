import com.auth0.jwt.algorithms.Algorithm

[
    port: 8000,
    https: false,

    authAlgorithm: Algorithm.HMAC256('YOUR_SECRET')
]