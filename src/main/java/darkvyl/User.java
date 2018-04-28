package darkvyl;

class User {
    private String id;
    private String name;
    private String nick;
    private String profession;
    private String level;
    private int attack;

    User() {
    }

    User(String id, String name, String nick, String profession, String level, int attack) {
        this.id = id;
        this.name = name;
        this.nick = nick;
        this.profession = profession;
        this.level = level;
        this.attack = attack;
    }

    String getId() {
        return id;
    }

    User setId(String id) {
        this.id = id;
        return this;
    }

    String getName() {
        return name;
    }

    User setName(String name) {
        this.name = name;
        return this;
    }

    String getNick() {
        return nick;
    }

    User setNick(String nick) {
        this.nick = nick;
        return this;
    }

    String getProfession() {
        return profession;
    }

    User setProfession(String profession) {
        this.profession = profession;
        return this;
    }

    String getLevel() {
        return level;
    }

    User setLevel(String level) {
        this.level = level;
        return this;
    }

    int getAttack() {
        return attack;
    }

    User setAttack(int attack) {
        this.attack = attack;
        return this;
    }

    @Override
    public String toString() {
        return "-> " + name + ". Также известен как: " + nick + ". Род деятельности и стаж: " + profession + " " + level + ".";
    }
}