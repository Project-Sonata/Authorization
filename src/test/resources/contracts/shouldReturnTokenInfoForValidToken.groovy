package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return token info if the token is valid")

    request {
        method POST()
        url("/token/info")
        body(token: "mikunakanoisthebestgirl")
        headers {
            contentType("application/json")
        }
    }
    response {
        status OK()
        body(
                valid: true,
                user_id: "1",
                scope: "read write",
                iat: anyInteger(),
                expires_in: positiveInt()
        )
    }
}