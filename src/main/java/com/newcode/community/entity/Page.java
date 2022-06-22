package com.newcode.community.entity;

public class Page {
    //当前页从前端获取
    private int current=1;
    //每一页的条数从前端获取
    private int limit=10;
    //总数据数从数据库查
    private int rows;
    //分页组件每一个按钮的的路径
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current > 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows > 1) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal() {
        if (rows % limit != 0) {
            return rows / limit + 1;
        } else {
            return rows / limit;
        }
    }
    public int getFrom(){
        int form=current - 2;
        return form < 1 ? 1 : form;
    }

    public int getTo(){
        int to=current + 2;
        int total=getTotal();
        return to > total ? total : to;
    }

}
