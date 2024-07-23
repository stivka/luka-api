package ee.stivka.luka.model;

public class DiscordRequest {

    private String content;

    public DiscordRequest() {
    }

    public DiscordRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
