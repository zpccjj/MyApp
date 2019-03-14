package util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2018/8/17.
 */

public class PrintUtils {

    /**
     * ��ӡֽһ�������ֽ�
     */
    private static final int LINE_BYTE_SIZE = 32;

    private static final int LEFT_LENGTH = 20;

    private static final int RIGHT_LENGTH = 12;

    /**
     * ��຺�������ʾ��������
     */
    private static final int LEFT_TEXT_MAX_LENGTH = 8;

    /**
     * СƱ��ӡ��Ʒ�����ƣ����޵���8����
     */
    public static final int MEAL_NAME_MAX_LENGTH = 8;

    private static OutputStream outputStream = null;

    public static OutputStream getOutputStream() {
        return outputStream;
    }

    public static void setOutputStream(OutputStream outputStream) {
        PrintUtils.outputStream = outputStream;
    }


    /**
     * ��ӡ����
     *
     * @param text Ҫ��ӡ������
     */
    public static void printText(String text) {
        try {
            byte[] data = text.getBytes("gbk");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
        } catch (IOException e) {
            //Toast.makeText(this.context, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * ���ô�ӡ��ʽ
     *
     * @param command ��ʽָ��
     */
    public static void selectCommand(byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            //Toast.makeText(this.context, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * ��λ��ӡ��
     */
    public static final byte[] RESET = {0x1b, 0x40};

    /**
     * �����
     */
    public static final byte[] ALIGN_LEFT = {0x1b, 0x61, 0x00};

    /**
     * �м����
     */
    public static final byte[] ALIGN_CENTER = {0x1b, 0x61, 0x01};

    /**
     * �Ҷ���
     */
    public static final byte[] ALIGN_RIGHT = {0x1b, 0x61, 0x02};

    /**
     * ѡ��Ӵ�ģʽ
     */
    public static final byte[] BOLD = {0x1b, 0x45, 0x01};

    /**
     * ȡ���Ӵ�ģʽ
     */
    public static final byte[] BOLD_CANCEL = {0x1b, 0x45, 0x00};

    /**
     * ��߼ӱ�
     */
    public static final byte[] DOUBLE_HEIGHT_WIDTH = {0x1d, 0x21, 0x11};

    /**
     * ��ӱ�
     */
    public static final byte[] DOUBLE_WIDTH = {0x1d, 0x21, 0x10};

    /**
     * �߼ӱ�
     */
    public static final byte[] DOUBLE_HEIGHT = {0x1d, 0x21, 0x01};

    /**
     * ���岻�Ŵ�
     */
    public static final byte[] NORMAL = {0x1d, 0x21, 0x00};

    /**
     * ����Ĭ���м��
     */
    public static final byte[] LINE_SPACING_DEFAULT = {0x1b, 0x32};

    /**
     * �����м��
     */
//	public static final byte[] LINE_SPACING = {0x1b, 0x32};//{0x1b, 0x33, 0x14};  // 20���м�ࣨ0��255��


//	final byte[][] byteCommands = {
//			{ 0x1b, 0x61, 0x00 }, // �����
//			{ 0x1b, 0x61, 0x01 }, // �м����
//			{ 0x1b, 0x61, 0x02 }, // �Ҷ���
//			{ 0x1b, 0x40 },// ��λ��ӡ��
//			{ 0x1b, 0x4d, 0x00 },// ��׼ASCII����
//			{ 0x1b, 0x4d, 0x01 },// ѹ��ASCII����
//			{ 0x1d, 0x21, 0x00 },// ���岻�Ŵ�
//			{ 0x1d, 0x21, 0x11 },// ��߼ӱ�
//			{ 0x1b, 0x45, 0x00 },// ȡ���Ӵ�ģʽ
//			{ 0x1b, 0x45, 0x01 },// ѡ��Ӵ�ģʽ
//			{ 0x1b, 0x7b, 0x00 },// ȡ�����ô�ӡ
//			{ 0x1b, 0x7b, 0x01 },// ѡ���ô�ӡ
//			{ 0x1d, 0x42, 0x00 },// ȡ���ڰ׷���
//			{ 0x1d, 0x42, 0x01 },// ѡ��ڰ׷���
//			{ 0x1b, 0x56, 0x00 },// ȡ��˳ʱ����ת90��
//			{ 0x1b, 0x56, 0x01 },// ѡ��˳ʱ����ת90��
//	};

    /**
     * ��ӡ����
     *
     * @param leftText  �������
     * @param rightText �Ҳ�����
     * @return
     */
    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);

        // �������������м�Ŀո�
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    /**
     * ��ӡ����
     *
     * @param leftText   �������
     * @param middleText �м�����
     * @param rightText  �Ҳ�����
     * @return
     */
    @SuppressLint("NewApi")
    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // ��������ʾ LEFT_TEXT_MAX_LENGTH ������ + ������
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // ����������ֺ��м����ֵĿո񳤶�
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // �����Ҳ����ֺ��м����ֵĿո񳤶�
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        // ��ӡ��ʱ���֣����ұߵ���������ƫ��һ���ַ���������Ҫɾ��һ���ո�
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        return sb.toString();
    }

    /**
     * ��ȡ���ݳ���
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * ��ʽ����Ʒ���ƣ������ʾMEAL_NAME_MAX_LENGTH����
     *
     * @param name
     * @return
     */
    public static String formatMealName(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        if (name.length() > MEAL_NAME_MAX_LENGTH) {
            return name.substring(0, 8) + "..";
        }
        return name;
    }

}
