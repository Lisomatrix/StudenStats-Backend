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
    PARENTE{
        public String toString() { return "ROLE_PARENT"; }
    },
    ADMIN{
        public String toString() { return "ROLE_ADMIN"; }
    }
}
