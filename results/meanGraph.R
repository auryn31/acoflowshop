iteration_0 <- read.csv(file="current_iteration_0.csv", header=FALSE, sep=",")
iteration_1 <- read.csv(file="current_iteration_1.csv", header=FALSE, sep=",")
iteration_2 <- read.csv(file="current_iteration_2.csv", header=FALSE, sep=",")
iteration_3 <- read.csv(file="current_iteration_3.csv", header=FALSE, sep=",")
iteration_4 <- read.csv(file="current_iteration_4.csv", header=FALSE, sep=",")

iteration <- c(iteration_0[1]$V1)
acoLength <- 600

bestSolutionACO_0 <- c(iteration_0[2]$V2[1:acoLength])
bestSolutionACO_1 <- c(iteration_1[2]$V2[1:acoLength])
bestSolutionACO_2 <- c(iteration_2[2]$V2[1:acoLength])
bestSolutionACO_3 <- c(iteration_3[2]$V2[1:acoLength])
bestSolutionACO_4 <- c(iteration_4[2]$V2[1:acoLength])
durationACO_0 <- c(iteration_0[3]$V3[1:acoLength])/1000
durationACO_1 <- c(iteration_1[3]$V3[1:acoLength])/1000
durationACO_2 <- c(iteration_2[3]$V3[1:acoLength])/1000
durationACO_3 <- c(iteration_3[3]$V3[1:acoLength])/1000
durationACO_4 <- c(iteration_4[3]$V3[1:acoLength])/1000

bestSolutionAICA_0 <- c(iteration_0[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_1 <- c(iteration_1[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_2 <- c(iteration_2[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_3 <- c(iteration_3[2]$V2[(acoLength+1):length(iteration)])
bestSolutionAICA_4 <- c(iteration_4[2]$V2[(acoLength+1):length(iteration)])
durationAICA_0 <- c(iteration_0[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_1 <- c(iteration_1[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_2 <- c(iteration_2[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_3 <- c(iteration_3[3]$V3[(acoLength+1):length(iteration)])/1000
durationAICA_4 <- c(iteration_4[3]$V3[(acoLength+1):length(iteration)])/1000

bestSolutionACO <- c()
for( i in 1:length(bestSolutionACO_0)) {
    bestSolutionACO[i] <- mean(c(bestSolutionACO_0[i], bestSolutionACO_1[i], bestSolutionACO_2[i], bestSolutionACO_3[i], bestSolutionACO_4[i]))
}

durationACO <- c()
for( i in 1:length(durationACO_0)) {
    durationACO[i] <- mean(c(durationACO_0[i], durationACO_1[i], durationACO_2[i], durationACO_3[i], durationACO_4[i]))
}

bestSolutionAICA <- c()
for( i in 1:length(bestSolutionAICA_0)) {
    bestSolutionAICA[i] <- mean(c(bestSolutionAICA_0[i], bestSolutionAICA_1[i], bestSolutionAICA_2[i], bestSolutionAICA_3[i], bestSolutionAICA_4[i]))
}

durationAICA <- c()
for( i in 1:length(durationAICA_0)) {
    durationAICA[i] <- mean(c(durationAICA_0[i], durationAICA_1[i], durationAICA_2[i], durationAICA_3[i], durationAICA_4[i]))
}

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

drawPlot('result_time_mean.jpg', 'ACO Flow Shop', 'Dauer in s', 'Länge', durationACO, durationAICA, bestSolutionACO, bestSolutionAICA)