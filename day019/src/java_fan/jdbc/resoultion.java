package java_fan.jdbc;

public class resoultion {
    public static void main(String[] args) {
        int[] nums = new int[]{2, 7, 7, 15};
        int target = 14;
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 1; j <= nums.length - 1; j++) {
                if (nums[i] + nums[j] == target) {
                    if (i > j) {
                        break;
                    }else if (i == j){
                        break;
                    }else {
                        System.out.println("[" + i + " " + j + "]");
                    }
                }
            }
        }


    }
}

