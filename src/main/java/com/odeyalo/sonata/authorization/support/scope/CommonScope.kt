package com.odeyalo.sonata.authorization.support.scope

class CommonScope(name: String,
                  description: String,
                  supportedRoles: Set<String>) :
    AbstractScope(name, description, supportedRoles)