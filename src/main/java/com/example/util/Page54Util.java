package com.example.util;

/**
 * 前五后四的分页
 * @author xuan
 * @create 2018-04-13 16:32
 **/
public class Page54Util {

    /**
     * @param totalPage 总页数
     * @param currentPage 当前页
     * @return
     */
    public static int[] getBeginAndEnd(int totalPage,int currentPage){
        int beginPage = currentPage - 5;
        if (beginPage < 1) {
            beginPage = 1;
        }
        int endPage = currentPage + 4;
        if (endPage > totalPage) {
            endPage = totalPage;
        }
        if (totalPage >= 10){
            if (beginPage == 1){
                endPage = 10;
            }
            if (endPage == totalPage){
                beginPage = totalPage - 9;
            }
        }
        return new int[]{beginPage, endPage};
    }
}
