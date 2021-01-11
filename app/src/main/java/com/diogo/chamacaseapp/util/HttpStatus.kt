package com.diogo.chamacaseapp.util


enum class HttpStatus(val code: Int,val message: String) {
    UNAUTHORIZED(401, "Não autorizado"),
    BAD_REQUEST(400, "Requisição incorreta"),
    REQUEST_TIMEOUT(408, "Requisição excedeu o tempo limite"),
    INTERNAL_SERVER_ERROR(500, "Erro interno no servidor"),
    GATEWAY_TIMEOUT(504, "Requisição excedeu o tempo limite"),
    NOT_FOUND(404, "Serviço remoto não encontrado"),
    UNKNOW(0, "Erro ao receber resposta do serviço remoto: código de erro não encontrado");

    companion object {
        fun find(code: Int) = HttpStatus.values().find {it.code == code } ?: UNKNOW
        fun find(name: String) = HttpStatus.values().find { it.name == name } ?: UNKNOW
    }
}