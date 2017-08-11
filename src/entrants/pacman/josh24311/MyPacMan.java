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

        /* D13 �g�LRandomd13.java���ͪ��H��Int */
        /*
         * Randomd13 dis = new Randomd13(); int D13 = dis.rand13;
         * System.out.println("D13_now : "+D13);
         */

        Constants.GHOST minGhost = null;
        Constants.GHOST minEdGhost = null;

        // �����H��Int 0~4
        Random rn = new Random();
        int randdir = rn.nextInt(5);



        //��ܬY�I���Ҧ��i���F�~
        //game.showNeighbour(1167);

        //���i������̪�
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If can't see these will be -1 so all fine there
            // ���Ҧ�����
            if (game.getGhostEdibleTime_new(ghost) == 0 && game.getGhostLairTime_new(ghost) == 0)
            {
                // �p�G�o�䰭���i���A�B���bŢ�l��
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
            	//�Yfor �]�� ����>0����bŢ�l��
            }
        }

        //�i������̪�
        for (Constants.GHOST ghost : Constants.GHOST.values())
        {
            // If it is > 0 then it is visible so no more PO checks
            //���Ҧ�����
            if (game.getGhostEdibleTime_new(ghost) > 0)
            {
                //�p�G�o�䰭�B��i�����A
                int EdghostLocation = game.getGhostCurrentNodeIndex_new(ghost);
                int disFromEdGh = game.getShortestPathDistance(current, EdghostLocation);
                //���o������p���F�̪�Z��
                if (disFromEdGh < minDistanceEdGh)
                {
                    //����FOR�j�餧��|���Z���̵u���i����
                    minDistanceEdGh = disFromEdGh;
                    minEdGhost = ghost;
                }
            }
        }

        //�T�{PP�O�_�s�b
        ArrayList<Integer> targets = new ArrayList<Integer>();
        for (int i = 0; i < powerPills.length; i++)   // check with power pills
        {
            // are available
            Boolean PowerpillStillAvailable = game.isPowerPillStillAvailable_new(i);
            // �o�������~�ӡA���אּgame.isPowerPillStillAvailable(i);
            if (PowerpillStillAvailable != null)
            {
                if (PowerpillStillAvailable)
                {
                    targets.add(powerPills[i]);
                    // �p�G�o�Ӥj�O�Y�s�b�A�h�s�Jtarget
                }
            }
        }
        //�T�{P�O�_�s�b
        ArrayList<Integer> targets_p = new ArrayList<Integer>();
        for (int i = 0; i < pills.length; i++)   // check with pills are available
        {
            Boolean pillStillAvailable = game.isPillStillAvailable_new(i);
            if (pillStillAvailable != null)
            {
                if (pillStillAvailable)
                {
                    targets_p.add(pills[i]);
                    // �p�G�o���ĤY�s�b�A�h�s�Jtarget_p
                }
            }
        }
        if(!targets_p.isEmpty())  //�٦�p
        {
        	//System.out.println("�٦�p");
            //�ഫ
            int[] targetsArray_p = new int[targets_p.size()];
            for (int i = 0; i < targetsArray_p.length; i++)
            {
                targetsArray_p[i] = targets_p.get(i);
            }
            closestP = game.getClosestNodeIndexFromNodeIndex(current, targetsArray_p, Constants.DM.PATH);
            disTonearestP = game.getShortestPathDistance(current, closestP);
        }
        if (!targets.isEmpty())   //�٦�PP�s�b�����p
        {
            //�ഫ
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
            
            //�P�_���}�l=====================================================================
            // ���i�����s�b
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
            else //�i�������s�b
            {
                if(minDistanceGh>=D2 && minDistanceGh <=D3 && disTonearestPp >=D6 && disTonearestPp <=D7)
                {
                    //�w�ƮI��϶��A��ı���M�I
                	//System.out.println("Coming Ghost: " + minGhost);
                    if(current==1089||current==1095||current==1125||current==1094||current==1124||current==1130)
                    {
                        ambush_stat = true;
                        System.out.println("Ambush NOW");
                    }
                    if(ambush_stat) //�bambush���A�U
                    {
                        if(minDistanceGh<=D1)
                        {
                            //���ӱ���
                            ambush_stat = false;
                            //System.out.println("Situation 3 : Too Close Eat PP");
                            //eat pp
                            return game.getNextMoveTowardsTarget(current, closestPp, Constants.DM.PATH);
                        }
                        else if(minDistanceGh >=D2)
                        {
                        	//�o�̭n�ץ�
                            //����������
                            ambush_stat = false;
                            //System.out.println("Situation 4 : Ghost GO Away Eat nearest p");
                            //eat n_p
                            return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
                        }
                        else
                        {
                        	//�����@
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
                    else //�Dambush���A����ı���M�I
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
        	if(minEdGhost!=null) //���i����
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
        	else//�S���i����
        	{
        		//10
        		//���D: �S��PP�S���i���� �A���ۤv�S���F�@���A���ɴN�S�� minGhost ���ȤF*******************************
        		//�ո�: ���bŢ�l�̪��o�q�ɶ����H����
        		if(minGhost!=null)//�����bŢ�l��
        		{
        			ghostLocation_now = game.getGhostCurrentNodeIndex_new(minGhost);
            		nghWithNp = game.getShortestPathDistance(ghostLocation_now, closestP);
            		if(minDistanceGh<=D1 && nghWithNp<=D3)
            		{
            			//System.out.println("Situation 10 : Eat  remain p");
            			return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
            		}
            		else // ���װ����k
            		{
            			//�o�̭n��@�����������k
            			//System.out.println("Avoiding too close ghost");
            			return game.getNextMoveAwayFromTarget(current, ghostLocation_now, Constants.DM.PATH);
            		}
        		}
        		else //���bŢ�l�̡A���ɨS��PP�A�S���i����
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
		1.�����������k -L313
		  Situation 7 ��@�A����u�������A�٭n�h�Y�p�ĤY�A
		2.�S��PP�S���i�����A���ɧ䤣��minGhost�A�ݦA�[�@�ӧP�_ -L303
		3.D2 D6 �Ȫ��D�� L190
		
		
		*/
        // System.out.println("MOVE : "+);
        //System.out.println("RAND_MOVE : " + myMove);
        //return myMove;
        //System.out.println("Follow nearest pill");
        return game.getNextMoveTowardsTarget(current, closestP, Constants.DM.PATH);
        //return MOVE.LEFT;
    }
}
