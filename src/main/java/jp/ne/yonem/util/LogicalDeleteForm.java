package jp.ne.yonem.util;

import lombok.Data;

@Data
public class LogicalDeleteForm {

    public LogicalDeleteForm(int id) {
        this.id = id;
    }

    int id;
    int page;
}
