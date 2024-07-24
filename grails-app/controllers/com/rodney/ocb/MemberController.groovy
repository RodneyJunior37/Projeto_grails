package com.rodney.ocb

class MemberController {

    MemberService memberService

    //exibir a lista de membros
    def index() {
        def response = memberService.list(params)
        [memberList: response.list, total: response.count]
    }

    //detalhes de um membro específico
    def details(Integer id) {
        def response = memberService.getById(id)
        if (!response) {
            redirect(controller: "member", action: "index")
        } else {
            [member: response]
        }
    }

    //formulário de criação de membro
    def create() {
        [member: flash.redirectParams]
    }

    //salvar um novo membro
    def save() {
        def response = memberService.save(params)
        if (!response.isSuccess) {
            flash.redirectParams = response.model
            flash.message = AppUtil.infoMessage(g.message(code: "unable.to.save"), false)
            redirect(controller: "member", action: "create")
        } else {
            flash.message = AppUtil.infoMessage(g.message(code: "saved"))
            redirect(controller: "member", action: "index")
        }
    }
    //exibir o formulário de edição de um membro
    def edit(Integer id) {
        if (flash.redirectParams) {
            [member: flash.redirectParams]
        } else {
            def response = memberService.getById(id)
            if (!response) {
                flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
                redirect(controller: "member", action: "index")
            } else {
                [member: response]
            }
        }
    }

    //atualizar os dados de um membro
    def update() {
        def response = memberService.getById(params.id)
        if (!response) {
            flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
            redirect(controller: "member", action: "index")
        } else {
            response = memberService.update(response, params)
            if (!response.isSuccess) {
                flash.redirectParams = response.model
                flash.message = AppUtil.infoMessage(g.message(code: "unable.to.update"), false)
                redirect(controller: "member", action: "edit")
            } else {
                flash.message = AppUtil.infoMessage(g.message(code: "updated"))
                redirect(controller: "member", action: "index")
            }
        }
    }

    // excluir um membro
    def delete(Integer id) {
        def response = memberService.getById(id)
        if (!response) {
            flash.message = AppUtil.infoMessage(g.message(code: "invalid.entity"), false)
            redirect(controller: "member", action: "index")
        } else {
            response = memberService.delete(response)
            if (!response) {
                flash.message = AppUtil.infoMessage(g.message(code: "unable.to.delete"), false)
            } else {
                flash.message = AppUtil.infoMessage(g.message(code: "deleted"))
            }
            redirect(controller: "member", action: "index")
        }
    }
}
