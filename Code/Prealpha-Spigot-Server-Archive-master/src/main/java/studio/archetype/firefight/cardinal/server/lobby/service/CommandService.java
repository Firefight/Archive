package studio.archetype.firefight.cardinal.server.lobby.service;

import studio.archetype.firefight.cardinal.common.util.Service;
import studio.archetype.firefight.cardinal.server.Cardinal;
import studio.archetype.firefight.cardinal.server.lobby.command.ForumVerify;
import studio.archetype.firefight.cardinal.server.lobby.command.Match;
import studio.archetype.firefight.cardinal.server.lobby.command.TestWeapons;

public class CommandService implements Service {
    @Override
    public void onEnable() {
        Cardinal.instance().getCommand("match").setExecutor(new Match());
        Cardinal.instance().getCommand("verify").setExecutor(new ForumVerify());
        Cardinal.instance().getCommand("testweapons").setExecutor(new TestWeapons());
    }

    @Override
    public void onDisable() {

    }
}
