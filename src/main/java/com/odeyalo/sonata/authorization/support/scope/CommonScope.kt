package com.odeyalo.sonata.authorization.support.scope

class CommonScope(
    name: String,
    description: String,
    supportedRoles: Set<String>,
    type: Scope.Type
) :
    AbstractScope(name, description, supportedRoles, type)