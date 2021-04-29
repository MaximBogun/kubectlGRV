import java.util.stream.Collectors
import static java.util.Arrays.stream
def readln = javax.swing.JOptionPane.&showInputDialog
def input = readln 'Connect?'
List commands = ['env', 'pass', 'user']
Map evnDic = [
        'tst': 'someSsh',
        'stg': 'someSsh',
        'int': 'someSsh'
]
Map commandOfArg = stream(input.split(" "))
        .filter { arg -> commands.findAll({ a -> arg.contains(a) }).any() }.
        flatMap({ arg -> stream(arg.strip().split("=")) })
        .collect(Collectors.toList()).toSpreadMap()
def host = evnDic[commandOfArg['env'] as String]
def sout = new StringBuilder(), serr = new StringBuilder()
def proc = "ssh ${host}".execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
def user = evnDic[commandOfArg['user'] as String]
def kubeCom = "kubelogin --insecure-skip-tls-verify --skip-open-browser --username ${user}".execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "out> ${sout}\nerr> ${serr}"
//ToDo fast login..

