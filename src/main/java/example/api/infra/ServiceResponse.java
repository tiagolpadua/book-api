package example.api.infra;

import java.util.ArrayList;
import java.util.List;

public class ServiceResponse<T> {
	private List<ServiceMessage> messages;
	private T data;

	public ServiceResponse() {
	}

	public ServiceResponse(T data) {
		this.data = data;
	}

	public ServiceResponse(T data, ServiceMessage message) {
		this.data = data;
		addMessage(message);
	}
	
	public ServiceResponse(T data, List<ServiceMessage> messages) {
		this.data = data;
		this.messages = messages;
	}

	public void addMessage(ServiceMessage message) {
		if (this.messages == null) {
			this.messages = new ArrayList<ServiceMessage>();
		}
		this.messages.add(message);
	}

	public ServiceResponse(List<ServiceMessage> messages) {
		this.messages = messages;
	}

	public ServiceResponse(ServiceMessage message) {
		addMessage(message);
	}

	public List<ServiceMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ServiceMessage> messages) {
		this.messages = messages;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
