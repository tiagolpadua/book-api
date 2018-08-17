package example.api.infra;

public class ServiceMessage {
	private MessageType type;
	private String message;

	public ServiceMessage() {
		super();
	}

	public ServiceMessage(String message) {
		this.message = message;
		this.type = MessageType.SUCCESS;
	}

	public ServiceMessage(MessageType type, String message) {
		this.type = type;
		this.message = message;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
