package indi.nevd.common.network;

/**
 * Created by nevd on 8/3/2017.
 */

public class BaseNetModel<Model> {
    public int errNo = 0, errno = 0, err_no = 0;
    public String errStr = "", errstr = "", err_msg = "";

    public Model data;

    @Override
    public String toString() {
        return "BaseNetModel{" +
                "errNo=" + errNo +
                ", errno=" + errno +
                ", err_no=" + err_no +
                ", errStr='" + errStr + '\'' +
                ", errstr='" + errstr + '\'' +
                ", err_msg='" + err_msg + '\'' +
                ", data=" + data +
                '}';
    }
}
