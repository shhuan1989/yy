function groupShootCost(data) {
    if (data == undefined) {
        return [];
    }

    var configs = {};
    $.each(data, function (index, config) {
        var projectId = config.projectId;
        if (configs[projectId] == undefined) {
            configs[projectId] = [];
        }
        configs[projectId].push(config);
    });

    var shootCosts = [];
    $.each(configs, function (projectId, tconfigs) {
        tconfigs.sort(function (c1, c2) {
            return c1.createTime - c2.createTime;
        });

        let actualCost = sum(tconfigs.map((c) => c.actualCost || 0));
        let budget = sum(tconfigs.map((c) => c.budget) || 0);

        shootCosts.push({
            configs: tconfigs,
            projectIdNumber: tconfigs[0].projectIdNumber,
            projectId: tconfigs[0].projectId,
            projectName: tconfigs[0].projectName,
            owner: tconfigs[0].owner,
            createTime: tconfigs[tconfigs.length-1].createTime,
            approvalRequest: tconfigs[tconfigs.length-1].approvalRequest,
            budget: budget,
            actualCost: actualCost,
            overBudgetPercentage: (actualCost - budget) / (budget > 0 ? budget : actualCost),
            diff: actualCost - budget
        });
    });
    shootCosts.sort(function (c1, c2) {
        if (c1.approvalRequest != undefined && c2.approvalRequest != undefined
            && c1.approvalRequest.status != undefined && c2.approvalRequest.status != undefined
            && c1.approvalRequest.status.id != c2.approvalRequest.status.id) {
            return c1.approvalRequest.status.id - c2.approvalRequest.status.id;
        } else {
            c1.createTime - c2.createTime;
        }
        return 0;
    });

    return shootCosts;
}
