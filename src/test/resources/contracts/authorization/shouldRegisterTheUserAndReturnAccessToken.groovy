package contracts.authorization

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description("Should register the user and return access token if credentials are valid")

    request {
        method POST()
        headers {
            contentType applicationJson()
        }
        url("/authorization/register")
        body(
                [
                        "email": "mikunakano@gmail.com",
                        "password": "nakano",
                        "birthdate": "2002-11-23",
                        "gender": "MALE",
                        "notification_enabled": true
                ]
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                [
                        "access_token": "MikuNakanoIsMyLove",
                        "expires_in": 3600
                ]
        )
    }
}