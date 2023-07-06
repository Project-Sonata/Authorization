package contracts.token

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
        headers {
            contentType("application/json")
        }

        body(
                [
                        "valid"     : true,
                        "user_id"   : "1",
                        "scope"     : "read write",
                        "iat"       : anyPositiveInt(),
                        "expires_in": anyPositiveInt()

                ])
    }
}