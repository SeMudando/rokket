package br.com.semudando.rokket

interface WebserviceUserValidator {
    fun validate(username: String, password: String): Boolean
}
