package contracts.authorization

import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.MediaType

Contract.make {
    description("Should confirm the code and return access token")

    request {
        method POST()
        url("/authorization/register/confirm")

        headers {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        }
        body(
                [
                        "data": "123"
                ]
        )
    }

    response {
        status OK()
        headers {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        }
        body(
                [
                        "confirmed": true,
                        "tokens"   : [
                                "access_token": [
                                        "value"     : "ILoveYouMiku",
                                        "expires_in": 3600
                                ]
                        ]
                ]
        )
    }
}
