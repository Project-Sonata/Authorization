package com.odeyalo.sonata.authorization.support.scope

import com.fasterxml.jackson.annotation.JsonProperty

class JsonScope(
    @JsonProperty("name") name: String,
    @JsonProperty("description") description: String,
    @JsonProperty("roles") supportedRoles: Set<String>
) : AbstractScope(name, description, supportedRoles)