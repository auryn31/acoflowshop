ResultMatrix <- read.csv(file="current.csv", header=FALSE, sep=",")

iteration <- c(ResultMatrix[1]$V1)
#bestSolutionACO <- c(ResultMatrix[2]$V2[1:length(iteration)])
#durationACO <- c(ResultMatrix[3]$V3[1:length(iteration)])/1000

bestSolutionACO <- c(ResultMatrix[2]$V2[1:600])
durationACO <- c(ResultMatrix[3]$V3[1:600])/1000
evaluationNumberACO <- c(ResultMatrix[4]$V4[1:600])
reworkPercentACO <- c(ResultMatrix[5]$V5[1:600])
simulationACO <- c(ResultMatrix[4]$V4[1:600])

bestSolutionAICA <- c(ResultMatrix[2]$V2[601:length(iteration)])
durationAICA <- c(ResultMatrix[3]$V3[601:length(iteration)])/1000
evaluationNumberAICA <- c(ResultMatrix[4]$V4[601:length(iteration)])
reworkPercentAICA <- c(ResultMatrix[5]$V5[601:length(iteration)])
simulationAICA <- c(ResultMatrix[4]$V4[601:length(iteration)])

# bestSolutionNEH <- c(ResultMatrix[2]$V2)[length(iteration):length(iteration)]
# durationNEH <- c(ResultMatrix[3]$V3)[length(iteration):length(iteration)]/1000

drawPlot <- function(name, title, xtitle, ytitle, x1, x2, y1, y2) {
    jpeg(name)
    yrange <- range(y1, y2)
    xrange <- range(x1, x2)
    xrange

    plot(x1, y1, type="l", ylim=yrange, xlim=xrange, col="blue", ann=FALSE)
    lines(x2, y2, type="l", col="green")
    box()

    title(main=title, col.main="red", font.main=4)
    title(xlab=xtitle)
    title(ylab=ytitle)
}

drawPlot('result_time.jpg', 'ACO Flow Shop', 'Dauer in s', 'Länge', durationACO, durationAICA, bestSolutionACO, bestSolutionAICA)
drawPlot('result_rework.jpg', 'ACO Flow Shop Rework', 'Dauer in s', 'Rework in %', durationACO, durationAICA, reworkPercentACO, reworkPercentAICA)
drawPlot('result_simulation.jpg', 'ACO Flow Shop Simulation', 'Simulation', 'Länge', simulationACO, simulationAICA, bestSolutionACO, bestSolutionAICA)

#legend(1, p_range[2], c("Lösungslänge", "Dauer"), cex=0.8, col=c("blue", "green", "red", "cyan", "gold"), pch=21:21, lty=1:1)
