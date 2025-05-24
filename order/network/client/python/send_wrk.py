import subprocess
from config import TARGET_URL, THREADS, CONNECTIONS, DURATION, NETNS

def run_wrk():
    cmd = [
        "sudo", "ip", "netns", "exec", NETNS,
        "wrk",
        f"-t{THREADS}",
        f"-c{CONNECTIONS}",
        f"-d{DURATION}",
        TARGET_URL
    ]

    with open("../logs/wrk_output.txt", "w") as f:
        process = subprocess.Popen(cmd, stdout=f, stderr=subprocess.PIPE)
        _, stderr = process.communicate()

    if stderr:
        print("WRK encountered an error:")
        print(stderr.decode())

if __name__ == "__main__":
    run_wrk()
