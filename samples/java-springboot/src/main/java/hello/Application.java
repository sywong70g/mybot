package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    System.out.println(arenaUpdate + " debug 3");

    System.out.println("AreenaUpdate is " + arenaUpdate);

    PlayerState myState = null;

    for (Map.Entry<String, PlayerState> playerMap : arenaUpdate.arena.state.entrySet() ) {
      PlayerState player = playerMap.getValue()；
      String playerId = playerMap.getKey();

      if (  playerId.equals(arenaUpdate._links.self.href) ) {
        System.out.println("Found myself " + playerId);
        myState = player;
      } else {
        System.out.println("Not myself " + playerId);
      }

    }

    String myDirection = myState.direction;


    for (Map.Entry<String, PlayerState> playerMap : arenaUpdate.arena.state.entrySet() ) {
      PlayerState player = playerMap.getValue()；
      String playerId = playerMap.getKey();
      if ( myState == player ) {
        System.out.println("Found myself!");
        next;
      }


      if ( "N".equals(myDirection) || "S".equals(myDirection) ) {
        if ( myState.x.intValue() == player.x ) {  // same vertical line
          int distance = player.y - myState.y;
          if ( distance > 0 && distance <= 3  && "N".equals(myDirection)) {
            return "F";
          } else {
            if ( distance <0  && distance >= -3  && "S".equals(myDirection)) {
              return "F";
            }
          }
        }
      }

      if ( "E".equals(myDirection) || "W".equals(myDirection) ) {
        if ( myState.y.intValue() == player.y ) {  // same horizontal line
          int distance = player.x - myState.x;
          if ( distance > 0 && distance <= 3  && "E".equals(myDirection)) {
            return "F";
          } else {
            if ( distance <0  && distance >= -3  && "W".equals(myDirection)) {
              return "F";
            }
          }
        }
      }

      String[] commands = new String[]{"F", "R"};
      int i = new Random().nextInt(2);
      return commands[i];


    }


    System.out.println("Return random....");
    String[] commands = new String[]{"F", "R", "L", "T"};
    int i = new Random().nextInt(4);
    return commands[i];
  }

}

