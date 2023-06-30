package contracts.authorization

import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.MediaType

Contract.make {
    description("Should return bad request if confirmation code is invalid")

    request {
        method POST()
        url("/authorization/register/confirm")

        headers {
            contentType(MediaType.APPLICATION_JSON_VALUE)
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
            contentType(MediaType.APPLICATION_JSON_VALUE)
        }
        body(
                [
                        "confirmed": false
                ]
        )
    }
}