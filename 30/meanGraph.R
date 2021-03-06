iteration_0 <- read.csv(file="current_iteration_0.csv", header=FALSE, sep=",")
iteration_1 <- read.csv(file="current_iteration_1.csv", header=FALSE, sep=",")
iteration_2 <- read.csv(file="current_iteration_2.csv", header=FALSE, sep=",")
iteration_3 <- read.csv(file="current_iteration_3.csv", header=FALSE, sep=",")
iteration_4 <- read.csv(file="current_iteration_4.csv", header=FALSE, sep=",")
iteration_5 <- read.csv(file="current_iteration_5.csv", header=FALSE, sep=",")
iteration_6 <- read.csv(file="current_iteration_6.csv", header=FALSE, sep=",")
iteration_7 <- read.csv(file="current_iteration_7.csv", header=FALSE, sep=",")
iteration_8 <- read.csv(file="current_iteration_8.csv", header=FALSE, sep=",")
iteration_9 <- read.csv(file="current_iteration_9.csv", header=FALSE, sep=",")

iteration <- c(iteration_0[1]$V1)
acoLength <- 1000

bestSolutionACO_0 <- c(iteration_0[2]$V2[1:acoLength])
bestSolutionACO_1 <- c(iteration_1[2]$V2[1:acoLength])
bestSolutionACO_2 <- c(iteration_2[2]$V2[1:acoLength])
bestSolutionACO_3 <- c(iteration_3[2]$V2[1:acoLength])
bestSolutionACO_4 <- c(iteration_4[2]$V2[1:acoLength])
bestSolutionACO_5 <- c(iteration_0[2]$V2[1:acoLength])
bestSolutionACO_6 <- c(iteration_1[2]$V2[1:acoLength])
bestSolutionACO_7 <- c(iteration_2[2]$V2[1:acoLength])
bestSolutionACO_8 <- c(iteration_3[2]$V2[1:acoLength])
bestSolutionACO_9 <- c(iteration_4[2]$V2[1:acoLength])
durationACO_0 <- c(iteration_0[3]$V3[1:acoLength])/1000
durationACO_1 <- c(iteration_1[3]$V3[1:acoLength])/1000
durationACO_2 <- c(iteration_2[3]$V3[1:acoLength])/1000
durationACO_3 <- c(iteration_3[3]$V3[1:acoLength])/1000
durationACO_4 <- c(iteration_4[3]$V3[1:acoLength])/1000
durationACO_5 <- c(iteration_0[3]$V3[1:acoLength])/1000
durationACO_6 <- c(iteration_1[3]$V3[1:acoLength])/1000
durationACO_7 <- c(iteration_2[3]$V3[1:acoLength])/1000
durationACO_8 <- c(iteration_3[3]$V3[1:acoLength])/1000
durationACO_9 <- c(iteration_4[3]$V3[1:acoLength])/1000
reworkPercentACO_0 <- c(iteration_0[5]$V5[1:acoLength])
reworkPercentACO_1 <- c(iteration_1[5]$V5[1:acoLength])
reworkPercentACO_2 <- c(iteration_2[5]$V5[1:acoLength])
reworkPercentACO_3 <- c(iteration_3[5]$V5[1:acoLength])
reworkPercentACO_4 <- c(iteration_4[5]$V5[1:acoLength])
reworkPercentACO_5 <- c(iteration_0[5]$V5[1:acoLength])
reworkPercentACO_6 <- c(iteration_1[5]$V5[1:acoLength])
reworkPercentACO_7 <- c(iteration_2[5]$V5[1:acoLength])
reworkPercentACO_8 <- c(iteration_3[5]$V5[1:acoLength])
reworkPercentACO_9 <- c(iteration_4[5]$V5[1:acoLength])
simulationACO_0 <- c(iteration_0[4]$V4[1:acoLength])
simulationACO_1 <- c(iteration_1[4]$V4[1:acoLength])
simulationACO_2 <- c(iteration_2[4]$V4[1:acoLength])
simulationACO_3 <- c(iteration_3[4]$V4[1:acoLength])
simulationACO_4 <- c(iteration_4[4]$V4[1:acoLength])
simulationACO_5 <- c(iteration_0[4]$V4[1:acoLength])
simulationACO_6 <- c(iteration_1[4]$V4[1:acoLength])
simulationACO_7 <- c(iteration_2[4]$V4[1:acoLength])
simulationACO_8 <- c(iteration_3[4]$V4[1:acoLength])
simulationACO_9 <- c(iteration_4[4]$V4[1:acoLength])

bestSolutionAICA_0 <- c(iteration_0[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_1 <- c(iteration_1[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_2 <- c(iteration_2[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_3 <- c(iteration_3[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_4 <- c(iteration_4[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_5 <- c(iteration_0[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_6 <- c(iteration_1[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_7 <- c(iteration_2[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_8 <- c(iteration_3[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_9 <- c(iteration_4[2]$V2[(acoLength+1):length(iteration)])
durationAICA_0 <- c(iteration_0[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_1 <- c(iteration_1[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_2 <- c(iteration_2[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_3 <- c(iteration_3[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_4 <- c(iteration_4[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_5 <- c(iteration_0[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_6 <- c(iteration_1[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_7 <- c(iteration_2[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_8 <- c(iteration_3[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_9 <- c(iteration_4[3]$V3[(acoLength+1):length(iteration)])/1000
reworkPercentAICA_0 <- c(iteration_0[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_1 <- c(iteration_1[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_2 <- c(iteration_2[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_3 <- c(iteration_3[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_4 <- c(iteration_4[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_5 <- c(iteration_0[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_6 <- c(iteration_1[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_7 <- c(iteration_2[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_8 <- c(iteration_3[5]$V5[(acoLength+1):length(iteration)])
reworkPercentAICA_9 <- c(iteration_4[5]$V5[(acoLength+1):length(iteration)])
simulationAICA_0 <- c(iteration_0[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_1 <- c(iteration_1[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_2 <- c(iteration_2[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_3 <- c(iteration_3[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_4 <- c(iteration_4[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_5 <- c(iteration_0[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_6 <- c(iteration_1[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_7 <- c(iteration_2[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_8 <- c(iteration_3[4]$V4[(acoLength+1):length(iteration)])
simulationAICA_9 <- c(iteration_4[4]$V4[(acoLength+1):length(iteration)])

bestSolutionACO <- c()
for( i in 1:length(bestSolutionACO_0)) {
    bestSolutionACO[i] <- mean(c(bestSolutionACO_0[i], bestSolutionACO_1[i], bestSolutionACO_2[i], bestSolutionACO_3[i], bestSolutionACO_4[i], bestSolutionACO_5[i], bestSolutionACO_6[i], bestSolutionACO_7[i], bestSolutionACO_8[i], bestSolutionACO_9[i]))
}

reworkPercentACO <- c()
for( i in 1:length(reworkPercentACO_0)) {
    reworkPercentACO[i] <- mean(c(reworkPercentACO_0[i], reworkPercentACO_1[i], reworkPercentACO_2[i], reworkPercentACO_3[i], reworkPercentACO_4[i], reworkPercentACO_5[i], reworkPercentACO_6[i], reworkPercentACO_7[i], reworkPercentACO_8[i], reworkPercentACO_9[i]))
}

durationACO <- c()
for( i in 1:length(durationACO_0)) {
    durationACO[i] <- mean(c(durationACO_0[i], durationACO_1[i], durationACO_2[i], durationACO_3[i], durationACO_4[i], durationACO_5[i], durationACO_6[i], durationACO_7[i], durationACO_8[i], durationACO_9[i]))
}

simulationACO <- c()
for( i in 1:length(simulationACO_0)) {
    simulationACO[i] <- mean(c(simulationACO_0[i], simulationACO_1[i], simulationACO_2[i], simulationACO_3[i], simulationACO_4[i], simulationACO_5[i], simulationACO_6[i], simulationACO_7[i], simulationACO_8[i], simulationACO_9[i]))
}

bestSolutionAICA <- c()
for( i in 1:length(bestSolutionAICA_0)) {
    bestSolutionAICA[i] <- mean(c(bestSolutionAICA_0[i], bestSolutionAICA_1[i], bestSolutionAICA_2[i], bestSolutionAICA_3[i], bestSolutionAICA_4[i], bestSolutionAICA_5[i], bestSolutionAICA_6[i], bestSolutionAICA_7[i], bestSolutionAICA_8[i], bestSolutionAICA_9[i]))
}

durationAICA <- c()
for( i in 1:length(durationAICA_0)) {    
    durationAICA[i] <- mean(c(durationAICA_0[i], durationAICA_1[i], durationAICA_2[i], durationAICA_3[i], durationAICA_4[i], durationAICA_5[i], durationAICA_6[i], durationAICA_7[i], durationAICA_8[i], durationAICA_9[i]))
}

reworkPercentAICA <- c()
for( i in 1:length(reworkPercentAICA_0)) {
    reworkPercentAICA[i] <- mean(c(reworkPercentAICA_0[i], reworkPercentAICA_1[i], reworkPercentAICA_2[i], reworkPercentAICA_3[i], reworkPercentAICA_4[i], reworkPercentAICA_5[i], reworkPercentAICA_6[i], reworkPercentAICA_7[i], reworkPercentAICA_8[i], reworkPercentAICA_9[i]))
}

simulationAICA <- c()
for( i in 1:length(simulationAICA_0)) {
    simulationAICA[i] <- mean(c(simulationAICA_0[i], simulationAICA_1[i], simulationAICA_2[i], simulationAICA_3[i], simulationAICA_4[i], simulationAICA_5[i], simulationAICA_6[i], simulationAICA_7[i], simulationAICA_8[i], simulationAICA_9[i]))
}

drawPlot <- function(name, title, xtitle, ytitle, x1, x2, y1, y2) {
    jpeg(name)
    yrange <- range(y1, y2)
    xrange <- range(x1, x2)
    xrange

    plot(x1, y1, type="l", ylim=yrange, xlim=xrange, col="blue", ann=FALSE)
    lines(x2, y2, type="l", col="green")
    box()

    title(main=title, xlab=xtitle, ylab=ytitle, font.main=4, cex.lab=1.4, cex.axis=1.4, cex.main=1.4, cex.sub=1.4)
    legend("topright", legend=c("ACO", "AICA"), col=c("blue", "green"), lty=1:1, cex=1.5)
}

drawPlot('result_time_mean.jpg', 'Flow Shop Dauer', 'Dauer in s', 'Länge', durationACO, durationAICA, bestSolutionACO, bestSolutionAICA)
drawPlot('result_rework_mean.jpg', 'Flow Shop Rework', 'Iteration', 'Rework in %', iteration[1:acoLength], iteration[(acoLength+1):length(iteration)], reworkPercentACO, reworkPercentAICA)
drawPlot('result_simulation_mean.jpg', 'Flow Shop Simulation', 'Simulation', 'Länge', simulationACO, simulationAICA, bestSolutionACO, bestSolutionAICA)
