package core.spring_core.singleton;

public class StatefulService {

    private int price;  // 상태를 유지하는 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);
        this.price = price;  // 여기가 문제!!!
    }

    public int getPrice() {
        return this.price;
    }

    /**
     * 무상태(stateless)로 설계하는 방법
    public int order(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);
        this.price = price;
        return price;
    }
     */
}
