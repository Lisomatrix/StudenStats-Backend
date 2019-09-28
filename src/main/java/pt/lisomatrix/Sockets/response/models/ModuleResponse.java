package pt.lisomatrix.Sockets.response.models;

import pt.lisomatrix.Sockets.models.Module;

public class ModuleResponse {

    private long moduleId;

    private String name;

    private long disciplineId;

    private long hours;

    public void populate(Module module) {

        setName(module.getName());
        setModuleId(module.getModuleId());
        setDisciplineId(module.getDiscipline().getDisciplineId());
        setHours(module.getHours());
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }
}
