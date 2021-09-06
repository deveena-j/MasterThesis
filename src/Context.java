
public class Context {

    private ServerAssignmentStrategyInterface strategy;

    public Context(ServerAssignmentStrategyInterface strategy){
        this.strategy = strategy;
    }

    public void executeStrategy(){
         strategy.assign_AP_Server();
         //strategy.calcUniServers();
    }
}// End of the Context class.  