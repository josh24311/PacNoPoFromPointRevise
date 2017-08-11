package entrants.pacman.josh24311;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;



/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getMove() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., entrants.pacman.username).
 */
public class MyPacMan extends PacmanController
{
    private MOVE myMove = MOVE.NEUTRAL;

    public MOVE getMove(Game game, long timeDue)
    {
        // Place your game logic here to play the game as Ms Pac-Man
        // Strategy 3: Go after the pills and power pills that we can see

        int current = game.getPacmanCurrentNodeIndex();
        int[] pills = game.getPillIndices();
        int[] powerPills = game.getPowerPillIndices();
        int minDistance = Integer.MAX_VALUE;
        int minDistanceGh = Integer.MAX_VALUE;
        int minDistanceEdGh = Integer.MAX_VALUE;
        int closestPp;
        int closestP = 0;

        int disTonearestPp = 0;
        int disTonearestP = 0;
        int disMinGhAndCloPp = 0;
        int nghWithNp = 0;
        int ghostLocation_now = 2;
        int ghlairtimemux = 1;
        

        int D1 = 12;
        int D2 = 16;
        int D3 = 50;
        int D4 = 32;
        int D5 = 8;
        int D6 = 0;
        int D7 = 50;
        int D8 = 30;
        int D9 = 6;
        int D10 = 10;
        int D11 = 17;
        int D12 = 28;
        int D13 = 25;
        boolean ambush_stat = false ;

        /* D13 經過Randomd13.java產生的隨機Int */
        /*
         * Randomd13 dis = new Randomd13(); int D13 = dis.rand13;
         * System.out.println("D13_now : "+D13);
         */

        Constants.GHOST minGhost = null;
        Constants.GHOST minEdGhost = null;

        // 產生隨機Int 0~4
        Random rn = new Random();
        int randdir = rn.nextInt(5);



        //顯示某點的所有可走鄰居
        //game.showNeighbour(1167);

        //不可食鬼找最近
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If can't see these will be -1 so all fine there
            // 對於所有的鬼
            if (game.getGhostEdibleTime_new(ghost) == 0 && game.getGhostLairTime_new(ghost) == 0)
            {
                // 如果這支鬼不可食，且不在籠子裡
                int ghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
                int disFromGh = game.getShortestPathDistance(current, ghostLocation);
                if (disFromGh < minDistanceGh)
                {
                    minDistanceGh = disFromGh;
                    minGhost = ghost;
                }
            }
            else if(game.getGhostLairTime(ghost)!=0)
            {
            	ghlairtimemux = ghlairtimemux * game.getGhostLairTime(ghost);
            	//若for 跑完 此值>0表全在籠子內
            }
        }

        //可食鬼找最近
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If it is > 0 then it is visible so no more PO checks
            //對於所有的鬼
            if (game.getGhostEdibleTime_new(ghost) > 0)
            {
                //如果這支鬼處於可食狀態
                int EdghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
                int disFromEdGh = game.getShortestPathDistance(current, EdghostLocation);
                //取得此鬼跟小精靈最近距離
                if (disFromEdGh < minDistanceEdGh)
                {
                    //做完FOR迴圈之後會找到距離最短的可食鬼
                    minDistanceEdGh = disFromEdGh;
                    minEdGhost = ghost;
                }
            }
        }

        //確認PP是否存在
        ArrayList<Integer> targets = new ArrayList<Integer>();
        for (int i = 0; i < powerPills.length; i++)   // check with power pills
        {
            // are available
            Boolean PowerpillStillAvailable = game.isPowerPillStillAvailable_new(i);
            // 這裡應為誤植，應改為game.isPowerPillStillAvailable(i);
            if (PowerpillStillAvailable != null)
            {
                if (PowerpillStillAvailable)
                {
                    targets.add(powerPills[i]);
                    // 如果這個大力丸存在，則存入target
                }
            }
        }
        //確認P是否存在
        ArrayList<Integer> targets_p = new ArrayList<Integer>();
        for (int i = 0; i < pills.length; i++)   // check with pills are available
        {
            Boolean pillStillAvailable = game.isPillStillAvailable_new(i);
            if (pillStillAvailable != null)
            {
                if (pillStillAvailable)
                {
                    targets_p.add(pills[i]);
                    // 如果這個藥丸存在，則存入target_p
                }
            }
        }
        if(!targets_p.isEmpty())  //還有p
        {
        	//System.out.println("還有p");
            //轉換
            int[] targetsArray_p = new int[targets_p.size()];
            for (int i = 0; i < targetsArray_p.length; i++)
            {
                targetsArray_p[i] = targets_p.get(i);
            }
            closestP = game.getClosestNodeIndexFromNodeIndex(current, targetsArray_p, Constants.DM.PATH);
            disTonearestP = game.getShortestPathDistance(current, closestP);
        }
        if (!targets.isEmpty())   //還有PP存在之狀況
        {
            //轉換
            int[] targetsArray = new int[targets.size()]; // convert from
            // ArrayList to
            // array

            for (int i = 0; i < targetsArray.length; i++)
            {
                targetsArray[i] = targets.get(i);
            }

            closestPp = game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH);
            disTonearestPp = game.getShortestPathDistance(current, closestPp);
           // game.getCurrentMaze().shortestPathDistances
            //System.out.println(game.getNextMoveTowardsTarget(948, 12, Constants.DM.PATH));
            
            //判斷式開始=====================================================================
            // 有可食鬼存在
            if(minEdGhost!=null)
            {
            	if(minDistanceEdGh<D13)
            	{
            		//System.out.println("Situation 1 : Go hunting");
            		return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minEdGhost), Constants.DM.PATH);
            	}
            	else if(minDistanceEdGh>D13&&minDistanceGh>D2 &&minDistanceGh<=D3&&disTonearestP<=D7)
            	{
            		//System.out.println("Situation 9 : Avoid danger eat n_p first");
            		return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
            	}
            	
            }
            else //可食鬼不存在
            {
                if(minDistanceGh>=D2 && minDistanceGh <=D3 && disTonearestPp >=D6 && disTonearestPp <=D7)
                {
                    //預備埋伏區間，察覺有危險
                	//System.out.println("Coming Ghost: " + minGhost);
                    if(current==1089||current==1095||current==1125||current==1094||current==1124||current==1130)
                    {
                        ambush_stat = true;
                        System.out.println("Ambush NOW");
                    }
                    if(ambush_stat) //在ambush狀態下
                    {
                        if(minDistanceGh<=D1)
                        {
                            //鬼太接近
                            ambush_stat = false;
                            //System.out.println("Situation 3 : Too Close Eat PP");
                            //eat pp
                            return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                        }
                        else if(minDistanceGh >=D2)
                        {
                        	//這裡要修正
                            //鬼漸漸遠離
                            ambush_stat = false;
                            //System.out.println("Situation 4 : Ghost GO Away Eat nearest p");
                            //eat n_p
                            return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                        }
                        else
                        {
                        	//撞牆實作
                            switch(current)
                            {
                            case 1089:
                                //System.out.println("Ambush~~~");
                                return MOVE.DOWN;
                            case 1095:
                                //System.out.println("Ambush~~~");
                                return MOVE.UP;
                            case 1125:
                                //System.out.println("Ambush~~~");
                                return MOVE.UP;
                            case 1094:
                                //System.out.println("Ambush~~~");
                                return MOVE.DOWN;
                            case 1124:
                                //System.out.println("Ambush~~~");
                                return MOVE.UP;
                            case 1130:
                                //System.out.println("Ambush~~~");
                                return MOVE.UP;
                            case 121:
                                //System.out.println("Ambush~~~");
                                return MOVE.LEFT;
                            case 126:
                                //System.out.println("Ambush~~~");
                                return MOVE.RIGHT;
                            }
                        }
                    }
                    else //非ambush狀態但察覺有危險
                    {
                        //System.out.println("Situation 2 : In Danger Find Somewhere To Hide");
                        switch(closestPp)
                        {
                        case 1143:
                            return game.getNextMoveTowardsTarget(current, 1095, Constants.DM.PATH);
                        case 1148:
                            return game.getNextMoveTowardsTarget(current, 1124, Constants.DM.PATH);
                        case 97:
                            return game.getNextMoveTowardsTarget(current, 121, Constants.DM.PATH);
                        case 102:
                            return game.getNextMoveTowardsTarget(current, 126, Constants.DM.PATH);
                        }
                    }
                }
                else if(minDistanceGh<=D1 && disTonearestPp <D5)
                {
                	//System.out.println("Situation 5 : Avoid tunnel Eat PP now");
                    //eat pp
                    return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                }
                else if(minDistanceGh>=D4 &&disTonearestPp >=D8 &&disTonearestP<=D9)
                {
                	//System.out.println("Situation 6 : Gh and pp too far Eat p first");
                    //eat n_p
                    return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                }
                else if(minDistanceGh>=D4&&disTonearestPp <=D8 && disTonearestP<=D11)
                {
                	//System.out.println("Situation 8: gh too far AVOID eat pp too early Eat p first");
                    //eat n_p
                    return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                }
            }
        }
        else // no pp exist
        {
        	if(minEdGhost!=null) //有可食鬼
        	{
        		if(minDistanceEdGh<D13)
        		{
        			//System.out.println("Eat Nearest Edgh");
        			return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minEdGhost), Constants.DM.PATH);
        		}
        		else if(minDistanceEdGh>D13 && minDistanceGh>D2 &&minDistanceGh<=D3 && disTonearestP<=D7)
        		{
        			//System.out.println("Edgh too far Eat p first");
        			return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        		}
        	}
        	else//沒有可食鬼
        	{
        		//10
        		//問題: 沒有PP沒有可食鬼 ，但自己又死了一次，此時就沒有 minGhost 的值了*******************************
        		//試解: 鬼在籠子裡的這段時間先隨機走
        		if(minGhost!=null)//鬼不在籠子裡
        		{
        			ghostLocation_now = game.getGhostCurrentNodeIndex_new(minGhost);
            		nghWithNp = game.getShortestPathDistance(ghostLocation_now, closestP);
            		if(minDistanceGh<=D1 && nghWithNp<=D3)
            		{
            			//System.out.println("Situation 10 : Eat  remain p");
            			return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
            		}
            		else // 躲避鬼走法
            		{
            			//這裡要實作鬼夾擊之走法
            			//System.out.println("Avoiding too close ghost");
            			return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
            		}
        		}
        		else //鬼在籠子裡，此時沒有PP，沒有可食鬼
        		{
        			switch (randdir)
        	        {
        	        case 0:
        	            myMove = MOVE.UP;
        	            break;
        	        case 1:
        	            myMove = MOVE.RIGHT;
        	            break;
        	        case 2:
        	            myMove = MOVE.DOWN;
        	            break;
        	        case 3:
        	            myMove = MOVE.LEFT;
        	            break;
        	        case 4:
        	            myMove = MOVE.NEUTRAL;
        	            break;
        	        }
        			return myMove;
        		}
        		
        	}
        }


        //dis(pac,n_gh)   - minDistanceGh
        //dis(pac,n_edgh) - minDistanceEdGh
        //dis(pac,n_pp)   - disTonearestPp
        //dis(pac,n_p)    - disTonearestP
        
        //System.out.println("=================go random=================");
        switch (randdir)
        {
        case 0:
            myMove = MOVE.UP;
            break;
        case 1:
            myMove = MOVE.RIGHT;
            break;
        case 2:
            myMove = MOVE.DOWN;
            break;
        case 3:
            myMove = MOVE.LEFT;
            break;
        case 4:
            myMove = MOVE.NEUTRAL;
            break;
        }
		/*
						Problem : 
		1.鬼夾擊之走法 -L313
		  Situation 7 實作，不能只遠離鬼，還要去吃小藥丸，
		2.沒有PP沒有可食鬼，此時找不到minGhost，需再加一個判斷 -L303
		3.D2 D6 值的挑選 L190
		
		
		*/
        // System.out.println("MOVE : "+);
        //System.out.println("RAND_MOVE : " + myMove);
        //return myMove;
        //System.out.println("Follow nearest pill");
        return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        //return MOVE.LEFT;
    }
}
