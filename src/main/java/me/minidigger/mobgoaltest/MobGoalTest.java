package me.minidigger.mobgoaltest;

import com.destroystokyo.paper.ai.Goal;
import com.destroystokyo.paper.ai.GoalFlag;
import com.destroystokyo.paper.ai.GoalType;
import com.destroystokyo.paper.ai.VanillaGoal;

import java.util.EnumSet;
import java.util.stream.Collectors;

import org.bukkit.EntityEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobGoalTest extends JavaPlugin {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mobgoaltest")) {
            if (!(sender instanceof Player)) {
                return true;
            }
            Player player = (Player) sender;
            Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
            String targetGoals = zombie.getGoals().getAllGoals(GoalType.TARGET).stream().map(goal -> goal.getKey().getKey()).collect(Collectors.joining(", "));
            String behaviorGoals = zombie.getGoals().getAllGoals(GoalType.BEHAVIOR).stream().map(goal -> goal.getKey().getKey()).collect(Collectors.joining(", "));
            player.sendMessage("Behavior goals: " + behaviorGoals);
            player.sendMessage("Target goals: " + targetGoals);
            zombie.getGoals().removeGoal(GoalType.TARGET, VanillaGoal.NEAREST_ATTACKABLE_TARGET);
            zombie.getGoals().removeGoal(GoalType.BEHAVIOR, zombie.getGoals().getAllGoals(GoalType.BEHAVIOR).iterator().next());
            String targetGoals2 = zombie.getGoals().getAllGoals(GoalType.TARGET).stream().map(goal -> goal.getKey().getKey()).collect(Collectors.joining(", "));
            player.sendMessage("Target goals2: " + targetGoals2);

            Villager villager = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
            villager.getGoals().addGoal(GoalType.TARGET, 1, new Goal() {

                @Override
                public boolean shouldActivate() {
                    return true;
                }

                @Override
                public void tick() {
                    villager.playEffect(EntityEffect.HURT);
                }

                @Override
                public NamespacedKey getKey() {
                    return new NamespacedKey(MobGoalTest.this, "villager_test");
                }

                @Override
                public EnumSet<GoalFlag> getFlags() {
                    return EnumSet.of(GoalFlag.LOOK);
                }
            });

            // remove test 1
            villager.getGoals().addGoal(GoalType.TARGET, 2, new Goal() {

                @Override
                public boolean shouldActivate() {
                    return true;
                }

                @Override
                public void tick() {
                    villager.playEffect(EntityEffect.FIREWORK_EXPLODE);
                }

                @Override
                public NamespacedKey getKey() {
                    return new NamespacedKey(MobGoalTest.this, "test");
                }

                @Override
                public EnumSet<GoalFlag> getFlags() {
                    return EnumSet.of(GoalFlag.LOOK);
                }
            });
            villager.getGoals().removeGoal(GoalType.TARGET, new NamespacedKey(MobGoalTest.this, "test"));

            // remove test 2
            Goal goal = new Goal() {

                @Override
                public boolean shouldActivate() {
                    return true;
                }

                @Override
                public void tick() {
                    villager.playEffect(EntityEffect.FIREWORK_EXPLODE);
                }

                @Override
                public NamespacedKey getKey() {
                    return new NamespacedKey(MobGoalTest.this, "test2");
                }

                @Override
                public EnumSet<GoalFlag> getFlags() {
                    return EnumSet.of(GoalFlag.LOOK);
                }
            };
            villager.getGoals().addGoal(GoalType.TARGET, 3, goal);
            villager.getGoals().removeGoal(GoalType.TARGET, goal);
            return true;
        }
        return false;
    }
}
