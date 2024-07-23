package com.rodney.ocb

class AuthenticationService {

    private static final String AUTHORIZED = "AUTHORIZED"

    // Método para definir a autorização do membro na sessão
    def setMemberAuthorization(Member member) {
        def authorization = [isLoggedIn: true, member: member]
        AppUtil.getAppSession()[AUTHORIZED] = authorization
    }

    // Método para realizar o login do usuário
    def doLogin(String email, String password){
        password = password.encodeAsMD5()  // Codifica a senha como MD5 (método inseguro, preferível usar bcrypt)
        Member member = Member.findByEmailAndPassword(email, password)
        if (member){
            setMemberAuthorization(member)  // Define a autorização na sessão
            return true  // Retorna verdadeiro se o login for bem-sucedido
        }
        return false  // Retorna falso se o login falhar
    }

    // Verifica se o usuário está autenticado
    boolean isAuthenticated(){
        def authorization = AppUtil.getAppSession()[AUTHORIZED]
        if (authorization && authorization.isLoggedIn){
            return true  // Retorna verdadeiro se estiver autenticado
        }
        return false  // Retorna falso se não estiver autenticado
    }

    // Retorna o objeto Member autenticado
    def getMember(){
        def authorization = AppUtil.getAppSession()[AUTHORIZED]
        return authorization?.member  // Retorna o membro associado à sessão, se existir
    }

    // Retorna o nome completo do membro autenticado
    def getMemberName(){
        def member = getMember()
        return "${member.firstName} ${member.lastName}"  // Retorna o nome completo do membro autenticado
    }

    // Verifica se o membro autenticado é um administrador
    def isAdministratorMember(){
        def member = getMember()
        if (member && member.memberType == GlobalConfig.USER_TYPE.ADMINISTRATOR){
            return true  // Retorna verdadeiro se o membro for um administrador
        }
        return false  // Retorna falso se o membro não for um administrador
    }
}
