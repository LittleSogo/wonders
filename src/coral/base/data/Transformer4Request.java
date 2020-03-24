package coral.base.data;

import javax.servlet.http.HttpServletRequest;

public class Transformer4Request extends Transformer4Base {

	private HttpServletRequest request;

	public Transformer4Request(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getParameter(String name) {
		return request.getParameter(name);
	}

	@Override
	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

}
