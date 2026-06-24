// 사람이 짜는 코드
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Practice {
    public static Integer peacePoint;
    public static String text;
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in,Charset.forName("MS949"));
        Random random = new Random();
        System.out.printf("플레이어의 이름을 정하세요:");
        String name = scan.nextLine();
        Player player = new Player(name,new ArrayList<Integer>(Arrays.asList(5,5)));
        
        ArrayList<Entity> zombies = new ArrayList<>();

        peacePoint = 100;
        Integer sleep = 0;
        boolean PLAYING = true;
        while (PLAYING) {

            if (random.nextInt(peacePoint) < 10) {
                ArrayList<Integer> spawnEmeryPosition = new ArrayList<>(List.of(
                    player.position.get(0)+(random.nextInt(13)-7),
                    player.position.get(1)+(random.nextInt(13)-7)
                ));
                System.out.println("좀비가 나타났습니다. 위치:" + spawnEmeryPosition);
                zombies.add(new Emery(10, "zomble", 3,spawnEmeryPosition,player));
            }

            

            if (sleep.equals(0)) {
                System.out.println("다음 행동을 입력하세요.(도움말:help)");
                String commandString = scan.nextLine();

                String[] commandList = commandString.split(" ");
                String command = commandList[0];
                Integer count = 0;
                if (commandList.length>=2) {
                    try {
                        count = Integer.parseInt(commandList[1]);
                    } catch (Exception e) {
                        System.out.println("횟수엔 정수를 입력해주시길 바랍니다.");
                    }
                }
                text = switch(command) {
                    case "help" -> {
                        yield "[방향]으로 [횟수:정수], 정해진 방향으로 횟수만큼 움직입니다.\n"+
                              "방향:앞,왼쪽,오른쪽,후방\n"+
                              "휴식 [횟수:정수], 횟수만큼 턴을 남비합니다.\n"+
                              "자결, 게임을 포기합니다";
                              }
                    case "앞으로",
                         "후방으로", 
                         "왼쪽으로", 
                         "오른쪽으로" -> {
                            yield command + count + "만큼 이동합니다." + player.position;
                        }
                    case "휴식" -> count + "턴 동안 쉽니다.";
                    case "자결" -> {
                        PLAYING = false;
                        yield "플레이어가 자결했습니다. 게임 종료";
                    }
                    default -> "명령어를 다시 입력해주세요.";
                };
                System.out.println(text);
                for (int i = 0; i < count; i++) {
                    switch (command) {
                    case "앞으로":
                        player.move(0,1);
                        break;
                    case "후방으로":
                        player.move(0,-1);
                        break;
                    case "왼쪽으로":
                        player.move(-1,0);
                        break;
                    case "오른쪽으로":
                        player.move(1,0);
                        break;
                    }
                    player.levelUp();
                    Entity.updates();
                    }
            } else {
                peacePoint -= 10;
                sleep--;
                Entity.updates();
            }
            
            
        }
        // code at exit
        scan.close();
    }

    public static void inFighting(Entity entity1, Entity entity2) {
        // Random random = new Random();
        while (entity1.hp > 0 ||
               entity2.hp > 0
        ) {
         if (entity1.speed < entity2.speed) {
            entity1.dealing(entity2);
            entity2.dealing(entity1);
            } else if (entity1.speed < entity2.speed) {
            entity1.dealing(entity2);
            entity2.dealing(entity1);
            }
         }  
        String text = "";
        if (entity1.hp < 0) {
            text = entity1.name + "이/가 이겼습니다!";
        } else if (entity2.hp < 0) {
            text =  entity2.name + "이/가 이겼습니다!";
        }

        System.out.println(text);
    }
    

    public static ArrayList<Integer> applyInteger(ArrayList<Integer> TargetArrayList, Integer Index, Integer e) {
        // 어떤 리스트에 특정 Index 에 Integer 값을 넣어주는 함수
        // A = applyInteger(A,Index,e) 순으로 적을 것
        while (TargetArrayList.size() <= Index) {
            TargetArrayList.add(null);
        }
        TargetArrayList.set(Index,e);
        return TargetArrayList;
    }

    static class Dead extends Entity {
        protected Integer hp;
        protected String name;
        protected Integer power;
        protected ArrayList<Integer> position;

        public Dead (Integer hp,String name,Integer power,ArrayList<Integer> position) {
            super(hp, name, power, position);
        }
    }

    static class Player extends Entity {
        private Integer level;
        private int exp;

        public Player (String name, ArrayList<Integer> Position) {
            super(20,name,3,Position);
            this.level = 1;
            this.exp = 0;
            
        }

      @Override
      public void dealing(Entity others) {
        super.dealing(others);
        if (others.hp < 0) {
            this.exp += (others.hp+others.power)/level;
            peacePoint = 100;
        }
      }
        public void levelUp() {
            if (exp > level * 20) {
                exp = 0;
                power += 1 + level;
                maxHp += 2 + 2 * level;
                if (maxHp > hp) {
                    hp += (maxHp-hp)/2;
                }
                speed += level/2;
                level++;
            }
        }
    }

    static class Emery extends Entity {
        Entity Target;
        public Emery(Integer Hp,String Name,Integer power, ArrayList<Integer> position,Entity TargetEntity) {
            this.Target = TargetEntity;
            super(Hp, Name, power, position); 
        }

        @Override
        public void update() {
            move(speed/5, Target.position);
            if (iscolled()) {
                System.out.printf(this.name + "이/가" +Target.name + " 앞을 가로 막아섰습니다!");
                inFighting(this, Target);
            }
        }

        public boolean iscolled() {
            return getDistance(Target.position) == 0;
        }

    }

    public abstract static class Entity {
        public static ArrayList<Entity> entityList = new ArrayList<>();

        protected Integer hp;
        protected Integer maxHp;
        protected String name;
        protected Integer power;
        protected ArrayList<Integer> position;
        protected Integer speed;
        protected Integer defense;

        public Entity(Integer Hp, String Name, Integer Power, ArrayList<Integer> Position) {
            this.hp = Hp;
            this.maxHp = Hp;
            this.name = Name;
            this.power = Power;
            this.speed = 5;
            this.position = Position;
            this.defense = 0;
            entityList.add(this);
        }
        public Integer getDistance(ArrayList<Integer> TargetPosition) {
            Integer getX = Math.abs(this.position.get(1) - TargetPosition.get(1));
            Integer getY = Math.abs(this.position.get(0) - TargetPosition.get(0));
            
            return getX + getY;
        }
        public Integer getXDistance(ArrayList<Integer> TargetPosition) {
            Integer getX = this.position.get(0) - TargetPosition.get(0);
            
            return getX;
        }
        public Integer getYDistance(ArrayList<Integer> TargetPosition) {
            Integer getY = this.position.get(1) - TargetPosition.get(1);
            return getY;
        }

        public void move(Integer Step, ArrayList<Integer> TargetPosition) {
            Integer Xdiretion = getXDistance(TargetPosition);
            Integer Ydiretion = getYDistance(TargetPosition);
            if (this.position == null || this.position.size() < 2) {
                this.position.add(0);
                this.position.add(0);
            }
            Integer i = 0;
            while (i<Step) {
                Xdiretion = getXDistance(TargetPosition);
                Ydiretion = getYDistance(TargetPosition);
                if (Xdiretion > 0) {
                    i++;
                    this.position.set(1,this.position.get(1)+1);
                } else if (Xdiretion < 0) {
                    i++;
                    this.position.set(1,this.position.get(1)-1);
                } else {    }
                if (Ydiretion > 0) {
                    i++;
                    this.position.set(0,this.position.get(0)+1);
                } else if (Ydiretion < 0) {
                    i++;
                    this.position.set(0,this.position.get(0)-1);
                } else {    }
            }
        }
        public void move(Integer x, Integer y) {
            this.position = applyInteger(this.position,0,this.position.get(0)+x);
            this.position = applyInteger(this.position,1,this.position.get(1)+y);
        }
        public void getDeal(Integer DealAmount) {
            this.hp -= Math.max(1,DealAmount-this.defense);
            if (this.hp < 0) {
                this.kill();
            }
        }
        public void dealing(Entity others) {
            others.getDeal(this.power);
            if (others.hp < 0) {
                System.out.printf("%s가 %s를 죽였습니다.",this.name, others.name);
            }
        }
        public void getHeal(Integer HealAmount) {
            this.hp += HealAmount;
            if (this.hp > this.maxHp) {
                this.hp = this.maxHp;
            }
        }
        public void setPosition(Integer x, Integer y) {
            applyInteger(this.position, 0, x);
            applyInteger(this.position, 1, y);
        }
        public void kill() {
            entityList.set(entityList.indexOf(this),new Dead(this.hp, this.name, this.power, this.position));
        }
        public void defend() {
            this.defense = (this.defense+this.defense/2);
        }
        public void undefend() {
            this.defense = (this.defense-this.defense/2);
        }

        public void update() {}

        public static void updates() {
            for (Integer i=entityList.size()-1;i>=0;i--) {
                Entity entity = entityList.get(i);
                if (entity.hp > 0) {
                    entity.update();
                }

            }
            Iterator<Entity> Entities = entityList.iterator();
            while (Entities.hasNext()) {
                Entity entity = Entities.next();
                if (entity.getClass().getSimpleName().equals("Dead")) {
                    Entities.remove();
                }  
            }
        }
    }
}

