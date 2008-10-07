package org.hivedb.teamcity.plugin;

import java.io.File;

import jetbrains.buildServer.vcs.*;
import jetbrains.buildServer.agent.vcs.CheckoutOnAgentVcsSupport;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildProgressLogger;

import org.apache.log4j.Logger;
import org.hivedb.teamcity.plugin.commands.CloneCommand;
import org.hivedb.teamcity.plugin.commands.FetchCommand;

public class GitVcsOnAgent implements CheckoutOnAgentVcsSupport {
  Logger log = Logger.getLogger(GitVcsOnAgent.class);

  public GitVcsOnAgent(BuildAgentConfiguration agentConf) {
  }

  public void updateSources(BuildProgressLogger logger, File workingDirectory, VcsRoot root, String newVersion, IncludeRule includeRule)
   throws VcsException {
    log.info("updateSources: " + workingDirectory + " " + newVersion);
    
    GitConfiguration configuration = GitConfiguration.createAgentConfiguration(root, workingDirectory);
    if (!configuration.isProjectDirectoryARepository()) {
      workingDirectory.delete();
      CloneCommand cmd = new CloneCommand(configuration);
      cmd.run();
    }
    else {
      FetchCommand cmd = new FetchCommand(configuration);
      cmd.run();
    }
  }

  public String getName() {
    return "git";
  }
}
