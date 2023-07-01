package contracts.authorization

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return bad request if confirmation code is invalid")

    request {
        method POST()
        url("/authorization/register/confirm")

        headers {
            contentType("application/json")
        }
        body(
                [
                        "data": "900"
                ]
        )
    }
    response {
        status BAD_REQUEST()
        headers {
            contentType("application/json")
        }
        body(
                [
                        "confirmed": false
                ]
        )
    }
}