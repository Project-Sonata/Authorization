package contracts.token

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Return body that signals that token is invalid if token does not exist")

    request {
        method POST()
        url("/token/info")
        body(token: "invalidtoken")
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
                        valid     : false,
                        iat       : 0,
                        expires_in: 0
                ]
        )
    }
}
