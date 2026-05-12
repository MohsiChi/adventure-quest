package adventurequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestLog {
    private final List<Quest> accepted;
    private final List<Quest> inProgress;
    private final List<Quest> completed;
    private final Map<String, Integer> progressCounts;

    public QuestLog() {
        this.accepted = new ArrayList<>();
        this.inProgress = new ArrayList<>();
        this.completed = new ArrayList<>();
        this.progressCounts = new HashMap<>();
    }

    public void acceptQuest(Quest q) {
        if (!accepted.contains(q) && !inProgress.contains(q) && !completed.contains(q)) {
            accepted.add(q);
        }
    }

    public void startQuest(Quest q) {
        accepted.remove(q);
        if (!inProgress.contains(q)) {
            inProgress.add(q);
            progressCounts.put(q.getId(), 0);
        }
    }

    public void updateProgress(String enemyName) {
        for (Quest q : new ArrayList<>(inProgress)) {
            if (q.getTargetEnemy().equalsIgnoreCase(enemyName)) {
                int current = progressCounts.getOrDefault(q.getId(), 0);
                current++;
                progressCounts.put(q.getId(), current);
            }
        }
    }

    public Quest checkCompletion() {
        for (Quest q : new ArrayList<>(inProgress)) {
            int current = progressCounts.getOrDefault(q.getId(), 0);
            if (current >= q.getTargetCount()) {
                return q;
            }
        }
        return null;
    }

    public void completeQuest(Quest q) {
        inProgress.remove(q);
        progressCounts.remove(q.getId());
        if (!completed.contains(q)) {
            completed.add(q);
        }
    }

    public int getProgress(Quest q) {
        return progressCounts.getOrDefault(q.getId(), 0);
    }

    public List<Quest> getAccepted() { return new ArrayList<>(accepted); }
    public List<Quest> getInProgress() { return new ArrayList<>(inProgress); }
    public List<Quest> getCompleted() { return new ArrayList<>(completed); }

    public List<String> getCompletedIds() {
        List<String> ids = new ArrayList<>();
        for (Quest q : completed) ids.add(q.getId());
        return ids;
    }

    public List<String> getInProgressIds() {
        List<String> ids = new ArrayList<>();
        for (Quest q : inProgress) ids.add(q.getId());
        return ids;
    }

    public Map<String, Integer> getProgressCounts() {
        return new HashMap<>(progressCounts);
    }

    public void addCompletedQuest(Quest q) {
        accepted.remove(q);
        inProgress.remove(q);
        if (!completed.contains(q)) {
            completed.add(q);
        }
    }

    public void addInProgressQuest(Quest q, int progress) {
        accepted.remove(q);
        completed.remove(q);
        if (!inProgress.contains(q)) {
            inProgress.add(q);
        }
        progressCounts.put(q.getId(), progress);
    }

    public boolean isQuestAvailable(Quest q) {
        return !accepted.contains(q) && !inProgress.contains(q) && !completed.contains(q);
    }
}
