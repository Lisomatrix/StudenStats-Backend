package pt.lisomatrix.Sockets.constants;

public enum Roles {
    ALUNO {
        public String toString() {
            return "ROLE_ALUNO";
        }
    },
    PROFESSOR{
        public String toString() {
            return "ROLE_PROFESSOR";
        }
    },
    SECRETARIO(),
    PARENTE{
        public String toString() {
            return "ROLE_PARENTE";
        }
    }
}
