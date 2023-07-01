package contracts.authorization

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should confirm the code and return access token")

    request {
        method POST()
        url("/authorization/register/confirm")

        headers {
            contentType("application/json")
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
            contentType("application/json")
        }
        body(
                [
                        "confirmed": true,
                        "tokens"   : [
                                "access_token": [
                                        "value"     : "MikuNakanoIsMyLove",
                                        "expires_in": 3600
                                ]
                        ]
                ]
        )
    }
}
