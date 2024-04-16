#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "scheduler.h"
#include "FileIO.h"

//For the schedule functions, we can know the data length is >= 4 since it passed through the FileIO which checks this.

int FCFS(int* data, int len)
{
    int movement = 0;
    int curPos = data[1];

    movement += abs(curPos - data[3]);
    curPos = data[3];

    for (int ii = 4; ii < len; ii++ )
    {
        movement += abs(curPos - data[ii]);
        curPos = data[ii];
    }
    free(data);
    return movement;
}

int SSTF(int* data, int len)
{
    int movement = 0;
    int curPos = data[1];

    for (int ii = 3; ii < len; ii++ )
    {
        int minDif = data[0];
        int minIdx = 0;

        for (int jj = 3; jj < len; jj++)
        {
            if (abs(curPos-data[jj]) < minDif)
            {
                minDif = abs(curPos-data[jj]);
                minIdx = jj;
            }
        }

        movement += minDif;
        curPos = data[minIdx];
        data[minIdx] = data[0] * 2;
    }
    free(data);
    return movement;
}

int SCAN(int* data, int len)
{
    int movement = 0;
    int curPos = data[1];
    int direction = data[2] - curPos;

    if (direction < 0)
    {
        //going towards n
        direction = -1;
    } else
    {
        direction = 1;
        //going towards 0
    }
    
    //1 extra iter for the turn around.
    for (int ii = 3; ii <= len; ii++ )
    {
        int dif;
        int minDif = data[0];
        int minIdx = 0;

        for (int jj = 3; jj < len; jj++)
        {
            dif = (curPos-data[jj]) * direction;
            if (( dif < minDif) & (dif > 0))
            {
                minDif = dif;
                minIdx = jj;
            }
        }

        //didn't find any, time to turn around!
        if (minIdx == 0)
        {
            if (direction == 1)
            {
                movement += curPos;
                curPos = 0;
            }
            else
            {
                movement += data[0]-1 - curPos;
                curPos = data[0]-1;
            }
            direction *= -1;
        }
        else
        {
            movement += minDif;
            curPos = data[minIdx];
            data[minIdx] = data[0] * -2;
        }
    }
    free(data);
    return movement;
}

int CSCAN(int* data, int len)
{
    int movement = 0;
    int curPos = data[1];
    int direction = data[2] - curPos;

    if (direction < 0)
    {
        //going towards n
        direction = -1;
    } else
    {
        direction = 1;
        //going towards 0
    }
    
    //1 extra iter for the turn around.
    for (int ii = 3; ii <= len; ii++ )
    {
        int dif;
        int minDif = data[0];
        int minIdx = 0;

        for (int jj = 3; jj < len; jj++)
        {
            dif = (curPos-data[jj]) * direction;
            if (( dif < minDif) & (dif >= 0))
            {
                minDif = dif;
                minIdx = jj;
            }
        }

        //didn't find any, time to circle around!
        if (minIdx == 0)
        {
            if (direction == 1)
            {
                movement += (curPos + data[0]-1);
                curPos = data[0]-1;
            }
            else
            {
                movement += (data[0]-1 - curPos + data[0]-1);
                curPos = 0;
            }
        }
        else
        {
            movement += minDif;
            curPos = data[minIdx];
            data[minIdx] = data[0] * -2;
        }
    }
    free(data);
    return movement;
}

int LOOK(int* data, int len)
{
    int movement = 0;
    int curPos = data[1];
    int direction = data[2] - curPos;

    if (direction < 0)
    {
        //going towards n
        direction = -1;
    } else
    {
        direction = 1;
        //going towards 0
    }
    
    //1 extra iter for the turn around.
    for (int ii = 3; ii <= len; ii++)
    {
        int dif;
        int minDif = data[0];
        int minIdx = 0;

        for (int jj = 3; jj < len; jj++)
        {
            dif = (curPos-data[jj]) * direction;
            if (( dif < minDif) & (dif > 0))
            {
                minDif = dif;
                minIdx = jj;
            }
        }

        //didn't find any, time to turn around!
        if (minIdx == 0)
        {
            direction *= -1;
        }
        else
        {
            movement += minDif;
            curPos = data[minIdx];
            data[minIdx] = data[0] * -2;
        }
    }
    free(data);
    return movement;
}

int CLOOK(int* data, int len)
{ 
    int movement = 0;
    int curPos = data[1];
    int direction = data[2] - curPos;

    if (direction < 0)
    {
        //going towards n
        direction = -1;
    } else
    {
        direction = 1;
        //going towards 0
    }
    
    for (int ii = 3; ii < len; ii++ )
    {
        int dif;
        int minDif = data[0];
        int minIdx = 0;

        for (int jj = 3; jj < len; jj++)
        {
            dif = (curPos-data[jj]) * direction;
            if (( dif < minDif) & (dif >= 0))
            {
                minDif = dif;
                minIdx = jj;
            }
        }

        //didn't find any, time to circle around!
        if (minIdx == 0)
        {   
            //Find the request furthest away
            int maxDif = 0;
            int maxIdx = 0;
            for (int jj = 3; jj < len; jj++)
            {
                dif = curPos+data[jj]* direction;
                if (( dif > maxDif) & (data[jj] >= 0))
                {
                    maxDif = dif;
                    maxIdx = jj;
                }
            }
            
            if(direction == 1)
            {
                /*When going forward the curPos is added instead of subtracted
                While that's necassary to determine the next location, its not the 
                movement we want to add so it needs to be corrected*/
                minDif = maxDif-2*curPos;
            }
            else
            {
                minDif = maxDif;
            }
            
            minIdx = maxIdx;
        }
        movement += minDif;
        curPos = data[minIdx];
        data[minIdx] = data[0] * -2;
        
    }
    free(data);
    return movement;
}
