package com.widetns.webapp.framework.aop.validation;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RequestValidator {

    private final HttpServletRequest request;

    public RequestValidator(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @param fields        �ʼ� �Է��� �ʿ��� �ʵ� ���
     * @param isAcceptBlank blank ��뿩��
     */
    public void checkRequired(String[] fields, boolean isAcceptBlank) {
        StringBuilder sb = new StringBuilder(100);
        for (String field : fields) {
            String param = request.getParameter(field);
            if (param == null || (!isAcceptBlank && param.length() == 0)) {
                sb.append(field).append(" is required field").append(", ");
            }
        }

        if (!sb.toString().equals("")) {
            request.setAttribute("errMessage", sb.substring(0, sb.toString().length() - 2));
        }
    }

    /**
     * <p>
     * �Է� �Ķ���Ϳ� ���ؼ� Ư�� ������ �ؼ��ϰ� �ִ��� ���θ� ����
     * </p>
     * <p>
     * <b>WARNING</b> null �Ǵ� ����('') ���� �Ķ���ͷ� ���޵Ǿ��� ���, ������ �������� �ʰ� return �Ѵ�.
     * <p>
     * <h2>format expression</h2> field_name[start_length](option=number&exp=/reg_exp/)<br/>
     * �ʵ��� ���̴� []�� �����ϰ�, �ʵ��� ���� ������ ()�� ����ִ´�. �������� �̹� ���ǵ� option�� ���Խ�(regexp)�� ���ǵ� ������ ����� �� ������, 2���� �̻��� �������� ���ÿ� �����
     * ���� "&amp;" ���ڸ� �̿��Ͽ� �����Ѵ�. options�� 2�� �̻��� ������ ������ ��� "|"�� �̿��Ͽ� �����Ѵ�. e.g.
     * <ul>
     * <li>name[3-5](option=no_space) - 3~5���ڷ� �����ǰ�, ������ ������</li>
     * <li>ssn[14](reg_exp=/\d{6}-[1234]\d{6}/) �ֹε��ȣ 14�ڸ��� "���� 6�ڸ�-(1~4)����6�ڸ��� ����</li>
     * <li>card_number(options=number|no_space) ī�� ��ȣ�� ���� ������ ������ "���ڷ� �����ǰ�, ������ ���� ��"
     * </ul>
     * ���� �ʵ带 ���ÿ� ������ ��� ","�� �̿��Ͽ� ���� ���� �Է� <h3>pre defined options�� ����</h3> �����Ŀ��� option �׸� ���� �� �ִ� �׸�
     * <ul>
     * <li>no_space : ���� ��� ����</li>
     * <li>number : ���ڸ� ���(0~9, ., ,)�� ���</li>
     * <li>int : ����(0~9)�� ���</li>
     * <li>alpha : ���� alpha�� �Ͽ�</li>
     * <li>alphanum : ���� + ��� ���</li>
     * </ul>
     * �� �پ��� ������ �����ϱ� ���ؼ��� "exp" �ɼ��� ����Ͽ� ������ �߰��� �� �ִ�.
     *
     * @param formatExp �Է� ������ format expression (�� �ʵ�� ","�� ����)
     */
    public void checkFormat(String formatExp) throws ParameterLengthException {
        // TODO length �̿��� option �� exp �κ��� ���Ŀ� �����Ѵ�. (���� �׸� ������ �ʴ�.)
        String[] fields = formatExp.split(",");

        final Pattern lengthRe = Pattern.compile("\\[(.*)\\]");
        final Pattern paramRe = Pattern.compile("(.*?)\\[(.*?)\\]");

        for (String field : fields) {
            String paramName;
            Matcher paramMatcher = paramRe.matcher(field);
            if (!paramMatcher.find()) {
                continue;
            }
            paramName = paramMatcher.group(1);

            String param = request.getParameter(paramName);
            if (param == null || (param.length()) == 0)
                return;
            int length = param.length();

            Matcher m = lengthRe.matcher(field);
            if (m.find()) {
                int min = -1, max = -1;
                String s[] = (" " + m.group(1) + " ").split("-");
                if (s.length == 1) {
                    min = max = Integer.parseInt(s[0].trim());
                } else {
                    if (s[0].trim().length() != 0)
                        min = Integer.parseInt(s[0].trim());
                    if (s[1].trim().length() != 0)
                        max = Integer.parseInt(s[1].trim());
                }

                if ((min >= 0 && length < min) || (max >= 0 && length > max)) {
                    throw new ParameterLengthException(paramName, length, min, max);
                }
            }
        }
    }
}
