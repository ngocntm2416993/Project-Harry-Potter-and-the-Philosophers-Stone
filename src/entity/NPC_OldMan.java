package entity;

import main.GamePanel;
import java.awt.Rectangle;

public class NPC_OldMan extends Entity {
    
    public String[][] dialogues = new String[5][5];
    public int dialogueIndex = 0;
    public boolean isDisappearing = false;
    private int disappearCounter = 0;

    public NPC_OldMan(GamePanel gp){
        super(gp);
        direction = "down";
        speed = 0; // Đứng yên đợi người chơi đến va chạm
        
        // Thiết lập khung va chạm  mặc định cho Cụ già
        solidArea = new Rectangle(6, 0, gp.tileSize - 12, gp.tileSize - 12);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getNPCImage();
        setDialogues();
    }

    public void getNPCImage (){
        up1 = setup("/npc/oldman_up_1.png");
        up2 = setup("/npc/oldman_up_2.png");
        down1 = setup("/npc/oldman_down_1.png");
        down2 = setup("/npc/oldman_down_2.png");
        left1 = setup("/npc/oldman_left_1.png");
        left2 = setup("/npc/oldman_left_2.png");
        right1 = setup("/npc/oldman_right_1.png");
        right2 = setup("/npc/oldman_right_2.png");
    }

    // Đặt chuỗi hội thoại hướng dẫn nhiệm vụ tại đây
    public void setDialogues() {
        // --- DIALOGUES CHO MAP 1 ---
        dialogues[1][0] = "Chào cậu! Cuối cùng cậu cũng đã vượt qua thử thách trước để tới đây.";
        dialogues[1][1] = "Căn phòng này ẩn chứa rất nhiều cạm bẫy nguy hiểm không lường trước.";
        dialogues[1][2] = "Hãy tìm các mật hiệu ẩn giấu trên các khối đá để mở cửa.";
        dialogues[1][3] = "Thời gian không còn nhiều, ta đi trước đây. Chúc may mắn!";

        // --- DIALOGUES CHO MAP 2 ---
        dialogues[2][0] = "Ồ, cậu đã sống sót và đặt chân được tới tầng thứ 2 này rồi sao?";
        dialogues[2][1] = "Kể từ đây, các câu đố sẽ không còn đơn giản như trước nữa đâu.";
        dialogues[2][2] = "Hãy chú ý đến những bức thư cổ nằm rải rác trong thư viện này.";
        dialogues[2][3] = "Hãy cẩn trọng từng bước đi, ta phải đi lo việc tiếp theo đây!";

        // --- DIALOGUES CHO MAP 3 ---
        //dialogues[3][0] = "Khá khen cho lòng kiên trì của cậu khi đến được mê cung Map 3 này.";
        //dialogues[3][1] = "Không gian ở đây liên tục biến đổi, lối ra có thể nằm ở nơi không ngờ nhất.";
        //dialogues[3][2] = "Giữ vững tinh thần tỉnh táo, đừng để các ảo ảnh đánh lừa thị giác.";
        //dialogues[3][3] = "Cố lên, thử thách sắp đi đến hồi kết rồi!";

        // --- DIALOGUES CHO MAP 4 ---
        dialogues[4][0] = "Cậu làm tốt lắm! Đây đã là căn phòng thử thách cuối cùng.";
        dialogues[4][1] = "Vị thủ lĩnh tối cao đang đợi cậu ở ngay phía sau cánh cửa lớn kia.";
        dialogues[4][2] = "Hãy chuẩn bị tinh thần và trang bị thật kỹ trước khi bước vào.";
        dialogues[4][3] = "Ta đã hoàn thành nhiệm vụ dẫn đường. Hẹn gặp lại cậu ở thế giới bên ngoài!";
    
    }

    @Override
    public void setAction() {
        if (isDisappearing) {
            direction = "up";
            speed = 2; 
        } else {
            speed = 0; 
        }
    }

    @Override
    public void update() {

        if(!isDisappearing){
            return;
        }

        super.update();

        disappearCounter++;

        if(disappearCounter > 100){
            for(int i = 0; i < gp.npc.length; i++){
                if(gp.npc[i] == this){
                    gp.npc[i] = null;
                    break;
                }
            }
        }
    }
}