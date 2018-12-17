package pt.lisomatrix.Sockets.constants;

public enum Roles {
    ALUNO {
        public String toString() {
            return "ALUNO";
        }
    },
    PROFESSOR{
        public String toString() {
            return "PROFESSOR";
        }
    },
    SECRETARIO(),
    PARENTE{
        public String toString() {
            return "PARENTE";
        }
    }
}
