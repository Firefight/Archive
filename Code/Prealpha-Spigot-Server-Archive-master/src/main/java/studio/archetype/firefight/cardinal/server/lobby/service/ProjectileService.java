package studio.archetype.firefight.cardinal.server.lobby.service;

import studio.archetype.firefight.cardinal.server.rtx.FutureProjectile;
import studio.archetype.firefight.cardinal.common.util.Service;
import studio.archetype.firefight.cardinal.server.Cardinal;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("SynchronizeOnNonFinalField") // onEnable not constructor, ignore. cannot be nulled
public class ProjectileService implements Service {
    private List<FutureProjectile> projectiles;
    private List<Future<?>> pending;
    private ExecutorService service;
    private int projectilesFired;
    private double pps;
    private int atick;
    private int tcount;

    @Override
    public void onEnable() {
        service = Executors.newWorkStealingPool(4);
        pending = new ArrayList<>();
        atick = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Cardinal.instance(), this::onAsyncTick, 0, 0);
        projectiles = new ArrayList<>();
        projectilesFired = 0;
        tcount = 0;
        pps = 0;
    }

    @Override
    public void onDisable() {
        service.shutdownNow().forEach(Runnable::run);
        Bukkit.getScheduler().cancelTask(atick);
    }

    public void queue(FutureProjectile projectile)
    {
        synchronized (projectiles)
        {
            projectiles.add(projectile);
        }
    }

    private void onAsyncTick() {
        synchronized (projectiles)
        {
            for(FutureProjectile i : projectiles)
            {
                pending.add(service.submit(() -> onTick(i)));
            }
        }

        while(!pending.isEmpty())
        {
            for(int i = pending.size()-1; i > 0; i--)
            {
                if(pending.get(i).isDone())
                {
                    pending.remove(i);
                }
            }


            try {
                // Yes, we are busy waiting, fuck off. We need this because you cant wait on work stealing pools
                // We need to keep this cycle synced and lag instead of backlog
                // noinspection BusyWait
                Thread.sleep(5);
            } catch (InterruptedException ignored) {
                break;
            }
        }

        tcount++;

        if(tcount % 100 == 0)
        {
            pps = projectilesFired / 5D;
            projectilesFired = 0;
        }
    }

    private void onTick(FutureProjectile projectile)
    {
        if(projectile.isDone())
        {
            synchronized (projectiles)
            {
                projectiles.remove(projectile);
                return;
            }
        }

        projectile.tick();
    }

    public int getActiveProjectiles()
    {
        return projectiles.size();
    }

    public double getProjectilesPerSecond()
    {
        return pps;
    }
}
